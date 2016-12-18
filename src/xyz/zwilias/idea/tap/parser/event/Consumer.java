package xyz.zwilias.idea.tap.parser.event;

public interface Consumer<T extends Event> {
    void consume(T event);
}
