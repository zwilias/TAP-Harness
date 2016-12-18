package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;
import xyz.zwilias.idea.tap.parser.event.Event;

import java.util.regex.Pattern;

public abstract class AbstractHandler implements LineHandler {
    protected final Pattern pattern;
    protected final State state;
    private final FireDelegate fireDelegate;

    public interface FireDelegate {
        public void fire(Event event);
    }

    public AbstractHandler(Pattern pattern, State state, FireDelegate fireDelegate) {
        this.pattern = pattern;
        this.state = state;
        this.fireDelegate = fireDelegate;
    }

    private <T extends Event> void fire(T event) {
        this.fireDelegate.fire(event);
    }

    void handlePrevious() {
        this.state.finishPreviousHandler();
    }

    @Override
    public boolean shouldHandle(@NotNull String line) {
        return this.pattern.matcher(line).matches();
    }

    public void finish() {
        this.fire(createEvent());
    }

    abstract protected Event createEvent();
}
