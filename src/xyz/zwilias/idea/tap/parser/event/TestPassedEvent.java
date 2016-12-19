package xyz.zwilias.idea.tap.parser.event;

public class TestPassedEvent implements Event {
    private final String tapLine;
    private final String diagnostics;

    public TestPassedEvent(String tapLine, String diagnostics) {
        this.tapLine = tapLine;
        this.diagnostics = diagnostics;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public String getTapLine() {
        return tapLine;
    }
}
