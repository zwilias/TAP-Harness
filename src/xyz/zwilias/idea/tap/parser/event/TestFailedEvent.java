package xyz.zwilias.idea.tap.parser.event;

public class TestFailedEvent extends AbstractTestEvent {
    public TestFailedEvent(String tapLine, String name, String testNumber, Object diagnostics) {
        super(tapLine, name, testNumber, diagnostics);
    }
}
