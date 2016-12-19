package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestPassedEvent;
import xyz.zwilias.idea.tap.parser.event.TestSkippedEvent;
import xyz.zwilias.idea.tap.parser.event.TestTodoEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPassedHandler extends AbstractHandler {
    private String line;
    private String diagnostics;

    private static final Pattern LINE_PATTERN = Pattern.compile(
            "^\\s*OK\\b\\s*(?<testnumber>\\d+)?\\s*(?<description>[^#]*)?\\s*(?:#\\s*)?((?<directive>\\S+)(?<directivecomment>.*))?$",
            Pattern.CASE_INSENSITIVE);

    public TestPassedHandler(State state, FireDelegate fireDelegate) {
        super(Pattern.compile("^\\s*OK\\b.*$", Pattern.CASE_INSENSITIVE), state, fireDelegate);
    }

    @Override
    public void handleLine(@NotNull String line) {
        this.diagnostics = null;
        this.line = line;
    }

    @Override
    public void addDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    @NotNull
    @Override
    public Event createEvent() {
        Matcher result = LINE_PATTERN.matcher(line);
        if (result.matches()) {
            String directive = result.group("directive");
            if ("SKIP".equalsIgnoreCase(directive)) {
                return new TestSkippedEvent(this.line, result.group("description"), result.group("testnumber"), diagnostics, result.group("directivecomment"));
            } else if ("TODO".equalsIgnoreCase(directive)) {
                return new TestTodoEvent(this.line, result.group("description"), result.group("testnumber"), diagnostics, result.group("directivecomment"));
            } else {
                return new TestPassedEvent(this.line, result.group("description"), result.group("testnumber"), diagnostics);
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
