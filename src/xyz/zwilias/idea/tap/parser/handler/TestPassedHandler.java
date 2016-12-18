package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestPassedEvent;

import java.util.regex.Pattern;

public class TestPassedHandler extends AbstractHandler {
    private String line;
    private Object diagnostics;

    public TestPassedHandler(State state, FireDelegate fireDelegate) {
        super(Pattern.compile("^\\s*OK\\s+.*$", Pattern.CASE_INSENSITIVE), state, fireDelegate);
    }

    @Override
    public void handle(@NotNull String line) {
        this.handlePrevious();

        this.line = line;
        this.diagnostics = null;
    }

    @Override
    public void addDiagnostics(Object diagnostics) {
        this.diagnostics = diagnostics;
    }

    @Override
    public Event createEvent() {
        return new TestPassedEvent();
    }
}
