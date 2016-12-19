package xyz.zwilias.idea.tap.parser.event;

public class TestTodoEvent extends AbstractTestEvent {
    private final String todoReason;

    public TestTodoEvent(String tapLine, String name, String testNumber, String diagnostics, String todoReason) {
        super(tapLine, name, testNumber, diagnostics);
        this.todoReason = todoReason;
    }

    public String getTodoReason() {
        return todoReason;
    }
}
