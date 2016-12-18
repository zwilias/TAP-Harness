package xyz.zwilias.idea.tap.parser;

import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.event.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser implements Runnable {
    private final InputStream stream;
    private final Map<Class<?>, Consumer<?>> consumerMap = new HashMap<>();
    private boolean closed = false;

    private static final Consumer<Event> DO_NOTHING = event -> {};

    private Parser(InputStream stream) {
        this.stream = stream;
    }


    private static final Pattern PATTERN_SKIP_TEST = Pattern.compile("^[^#]*#\\s*SKIP\\S*\\s+(.*)");
    private static final Pattern PATTERN_PASS_TEST = Pattern.compile("^\\s*OK\\s+.*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_FAIL_TEST = Pattern.compile("^\\s*NOT OK\\s+.*$", Pattern.CASE_INSENSITIVE);

    public void run() {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (PATTERN_SKIP_TEST.matcher(line).matches()) {
                fire(new TestSkippedEvent());
            } else if (PATTERN_PASS_TEST.matcher(line).matches()) {
                fire(new TestPassedEvent());
            } else if (PATTERN_FAIL_TEST.matcher(line).matches()) {
                fire(new TestFailedEvent());
            }
        }

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
        ((Consumer<T>)getConsumerFor(event.getClass())).consume(event);
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

    public static Parser parseStream(InputStream stream) {
        return new Parser(stream);
    }
}
