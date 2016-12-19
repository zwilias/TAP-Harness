package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestPassedEvent;

import java.util.regex.Pattern;

public class TestPassedHandler extends AbstractHandler {
    private String line;
    private String diagnostics;

    public TestPassedHandler(State state, FireDelegate fireDelegate) {
        super(Pattern.compile("^\\s*OK\\b.*$", Pattern.CASE_INSENSITIVE), state, fireDelegate);
    }

    @Override
    public void handleLine(@NotNull String line) {
        this.line = line;
        this.diagnostics = null;
    }

    @Override
    public void addDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    @NotNull
    @Override
    public Event createEvent() {
        return new TestPassedEvent(this.line, this.diagnostics);
    }
}
