package xyz.zwilias.idea.tap.parser;

import org.junit.Before;
import org.junit.Test;
import xyz.zwilias.idea.tap.parser.event.TestFailedEvent;
import xyz.zwilias.idea.tap.parser.event.TestPassedEvent;
import xyz.zwilias.idea.tap.parser.event.TestSkippedEvent;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    private OutputStreamWriter writer;

    @Before
    public void setUp() throws IOException {
        PipedOutputStream outputStream = new PipedOutputStream();
        writer = new OutputStreamWriter(outputStream);

        parser = Parser.parseStream(new PipedInputStream(outputStream));
    }

    private void writeStrings(String... strings) throws IOException, InterruptedException {
        Thread t = new Thread(parser);
        t.start();

        for (String string : strings) {
            writer.write(string);
            writer.write("\n");
        }

        writer.close();
        t.join();
    }

    @Test
    public void onTestPassed() throws IOException, InterruptedException {
        List<TestPassedEvent> events = new LinkedList<>();

        parser.onTestPassed(events::add);

        writeStrings(
                "NOT OK this is not ok",
                "OK this is a pass",
                "ok this is also a pass",
                "   ok",
                "ok SKIP"
        );

        assertThat(events.size(), is(3));
    }

    @Test
    public void onTestFailed() throws IOException, InterruptedException {
        List<TestFailedEvent> events = new LinkedList<>();

        parser.onTestFailed(events::add);

        writeStrings(
                "NOT OK this is not ok",
                "OK this is a pass",
                "ok this is also a pass",
                "   ok",
                "ok SKIP"
        );

        assertThat(events.size(), is(1));
    }

    @Test
    public void onTestSkipped() throws IOException, InterruptedException {
        List<TestSkippedEvent> events = new LinkedList<>();

        parser.onTestSkipped(events::add);

        writeStrings(
                "NOT OK this is not ok",
                "OK this is a pass",
                "ok this is also a pass",
                "   ok",
                "ok # SKIP more text here"
        );

        assertThat(events.size(), is(1));
    }

    @Test
    public void parseStream() throws IOException, InterruptedException {
        Thread t = new Thread(parser);
        t.start();

        writer.write("Hello world\n");

        writer.close();
        t.join();

        assertThat(parser.isClosed(), is(true));
    }
}
