package xyz.zwilias.idea.tap.parser.event;

import org.jetbrains.annotations.NotNull;

public class TestSkippedEvent implements TestEvent {
    private final TestEvent wrappedEvent;
    private final String skipReason;

    public TestSkippedEvent(@NotNull TestEvent wrappedEvent, String skipReason) {
        this.wrappedEvent = wrappedEvent;
        this.skipReason = skipReason;
    }

    public String getSkipReason() {
        return skipReason;
    }

    @Override
    public String getTapLine() {
        return wrappedEvent.getTapLine();
    }

    @Override
    public String getName() {
        return wrappedEvent.getName();
    }

    @Override
    public String getTestNumber() {
        return wrappedEvent.getTestNumber();
    }

    @Override
    public Object getDiagnostics() {
        return wrappedEvent.getDiagnostics();
    }
}
