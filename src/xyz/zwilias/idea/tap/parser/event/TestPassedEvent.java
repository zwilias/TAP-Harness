package xyz.zwilias.idea.tap.parser.event;

public class TestPassedEvent implements Event {
    private final String diagnostics;

    public TestPassedEvent(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getDiagnostics() {
        return diagnostics;
    }
}
