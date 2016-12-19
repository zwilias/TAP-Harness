package xyz.zwilias.idea.tap.parser.event;

public interface TestEvent extends Event {
    String getTapLine();

    String getName();

    String getTestNumber();

    Object getDiagnostics();
}
