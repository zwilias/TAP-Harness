package xyz.zwilias.idea.tap.parser;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.event.*;
import xyz.zwilias.idea.tap.parser.handler.*;

import java.io.InputStream;
import java.util.*;

public class Parser implements Runnable {
    private final InputStream stream;
    private final Map<Class<?>, Consumer<?>> consumerMap = new HashMap<>();
    private final State state;
    private boolean closed = false;
    private final List<LineHandler> handlers = new LinkedList<>();

    private static final Consumer<Event> DO_NOTHING = event -> {
    };

    private Parser(InputStream stream) {
        this.stream = stream;
        this.state = new State();

        handlers.addAll(Arrays.asList(
                new YamlHandler(this.state),
                new TestHandler(this.state, this::fire)
        ));
    }

    public void run() {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            this.handlers
                    .stream()
                    .filter(handler -> handler.shouldHandle(line))
                    .findFirst()
                    .ifPresent(handler -> {
                        handler.handle(line);
                        state.setPreviousHandler(handler);
                    });
        }

        state.finishPreviousHandler();
        scanner.close();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private <T extends Event> Consumer<T> getConsumerFor(Class<T> eventType) {
        return consumerMap.containsKey(eventType)
                ? (Consumer<T>) consumerMap.get(eventType)
                : (Consumer<T>) DO_NOTHING;
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void fire(T event) {
        ((Consumer<T>) getConsumerFor(event.getClass())).consume(event);
    }

    public Parser onTestPassed(Consumer<TestPassedEvent> consumer) {
        consumerMap.put(TestPassedEvent.class, consumer);
        return this;
    }

    public Parser onTestFailed(Consumer<TestFailedEvent> consumer) {
        consumerMap.put(TestFailedEvent.class, consumer);
        return this;
    }

    public Parser onTestSkipped(Consumer<TestSkippedEvent> consumer) {
        consumerMap.put(TestSkippedEvent.class, consumer);
        return this;
    }

    public Parser onTestTodo(Consumer<TestTodoEvent> consumer) {
        consumerMap.put(TestTodoEvent.class, consumer);
        return this;
    }

    public static Parser parseStream(InputStream stream) {
        return new Parser(stream);
    }
}
