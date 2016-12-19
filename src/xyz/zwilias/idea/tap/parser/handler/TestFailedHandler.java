package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestFailedEvent;

import java.util.regex.Pattern;

public class TestFailedHandler extends AbstractHandler {
    private String tapLine;
    private String diagnostic;

    public TestFailedHandler(State state, FireDelegate fireDelegate) {
        super(
                Pattern.compile("^\\s*NOT OK\\b.*$", Pattern.CASE_INSENSITIVE),
                state,
                fireDelegate
        );
    }

    @Override
    public void handleLine(@NotNull String line) {
        tapLine = line;
        diagnostic = null;
    }

    @Override
    public void addDiagnostics(String diagnostics) {
        diagnostic = diagnostics;
    }

    @NotNull
    @Override
    protected Event createEvent() {
        return new TestFailedEvent();
    }
}
