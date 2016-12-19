package xyz.zwilias.idea.tap.parser.event;

public class TestSkippedEvent extends AbstractTestEvent {
    private final String skipReason;

    public TestSkippedEvent(String tapLine, String name, String testNumber, String diagnostics, String skipReason) {
        super(tapLine, name, testNumber, diagnostics);
        this.skipReason = skipReason;
    }

    public String getSkipReason() {
        return skipReason;
    }
}
