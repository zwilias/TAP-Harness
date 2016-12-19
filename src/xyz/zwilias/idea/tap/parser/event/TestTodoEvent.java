package xyz.zwilias.idea.tap.parser.event;

import org.jetbrains.annotations.NotNull;

public class TestTodoEvent implements TestEvent {
    private final TestEvent wrappedEvent;
    private final String todoReason;

    public TestTodoEvent(@NotNull TestEvent wrappedEvent, String todoReason) {
        this.wrappedEvent = wrappedEvent;
        this.todoReason = todoReason;
    }

    public String getTodoReason() {
        return todoReason;
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
