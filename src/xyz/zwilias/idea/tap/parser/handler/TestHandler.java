package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestHandler extends AbstractHandler {
    private String line;
    private Object diagnostics;

    private static final Pattern LINE_PATTERN = Pattern.compile(
            "^\\s*(?<status>(NOT\\s+)?OK)\\b\\s*(?<testnumber>\\d+)?\\s*(?<description>[^#]*)?\\s*(?:#\\s*)?((?<directive>\\S+)(?<directivecomment>.*))?$",
            Pattern.CASE_INSENSITIVE);

    public TestHandler(State state, FireDelegate fireDelegate) {
        super(LINE_PATTERN, state, fireDelegate);
    }

    @Override
    public void handleLine(@NotNull String line) {
        this.diagnostics = null;
        this.line = line;
    }

    @Override
    public void addDiagnostics(Object diagnostics) {
        this.diagnostics = diagnostics;
    }

    @NotNull
    @Override
    public Event createEvent() {
        Matcher result = LINE_PATTERN.matcher(line);
        if (!result.matches()) {
            throw new IllegalStateException();
        }

        TestEvent event;
        String status = result.group("status");
        if ("OK".equalsIgnoreCase(status)) {
            event = new TestPassedEvent(this.line, result.group("description"), result.group("testnumber"), diagnostics);
        } else {
            event = new TestFailedEvent(this.line, result.group("description"), result.group("testnumber"), diagnostics);
        }

        String directive = result.group("directive");

        if ("SKIP".equalsIgnoreCase(directive)) {
            event = new TestSkippedEvent(event, result.group("directivecomment"));
        } else if ("TODO".equalsIgnoreCase(directive)) {
            event = new TestTodoEvent(event, result.group("directivecomment"));
        }

        return event;
    }
}
