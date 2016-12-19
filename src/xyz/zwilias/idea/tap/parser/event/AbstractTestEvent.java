package xyz.zwilias.idea.tap.parser.event;

public abstract class AbstractTestEvent implements TestEvent {
    protected final String tapLine;
    protected final String name;
    protected final String testNumber;
    protected final Object diagnostics;

    public AbstractTestEvent(String tapLine, String name, String testNumber, Object diagnostics) {
        this.tapLine = tapLine;
        this.name = name;
        this.testNumber = testNumber;
        this.diagnostics = diagnostics;
    }

    public String getTapLine() {
        return tapLine;
    }

    public String getName() {
        return name;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public Object getDiagnostics() {
        return diagnostics;
    }
}
