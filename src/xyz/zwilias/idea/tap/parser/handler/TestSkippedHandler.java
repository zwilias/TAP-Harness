package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;
import xyz.zwilias.idea.tap.parser.event.TestSkippedEvent;

import java.util.regex.Pattern;

public class TestSkippedHandler extends AbstractHandler {
    public TestSkippedHandler(State state, FireDelegate fireDelegate) {
        super(
                Pattern.compile("^[^#]*#\\s*SKIP.*$", Pattern.CASE_INSENSITIVE),
                state,
                fireDelegate
        );
    }

    @Override
    public void handleLine(@NotNull String line) {

    }

    @Override
    public void addDiagnostics(String diagnostics) {

    }

    @NotNull
    @Override
    protected Event createEvent() {
        return new TestSkippedEvent();
    }
}
