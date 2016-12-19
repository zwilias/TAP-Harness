package xyz.zwilias.idea.tap.parser.event;

public class TestPassedEvent extends AbstractTestEvent{

    public TestPassedEvent(String tapLine, String name, String testNumber, String diagnostics) {
        super(tapLine, name, testNumber, diagnostics);
    }
}
