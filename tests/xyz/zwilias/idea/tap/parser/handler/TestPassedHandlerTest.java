package xyz.zwilias.idea.tap.parser.handler;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.zwilias.idea.tap.parser.State;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class TestPassedHandlerTest {
    private TestPassedHandler handler;
    private AbstractHandler.FireDelegate fireDelegate;

    @Before
    public void setUp() throws Exception {
        State state = new State();
        handler = new TestPassedHandler(state, fireDelegate);
    }

    @After
    public void tearDown() {
        fireDelegate = null;
    }

    @Test
    @Parameters(method = "provideMatchingLines")
    public void shouldHandle_shouldHandleExpectedLines(String inputLine) {
        assertThat(handler.shouldHandle(inputLine), is(true));
    }

    @Test
    @Parameters(method = "provideNotMatchingLines")
    public void shouldHandle_unexpectedLines_shouldReturnFalse(String inputLine) {
        assertThat(handler.shouldHandle(inputLine), is(false));
    }

    public static List<String> provideMatchingLines() {
        return Arrays.asList(
                "OK",
                "ok",
                "    ok",
                "ok 1 this is a matching test # with comment",
                "    OK#something"
        );
    }

    public static List<String> provideNotMatchingLines() {
        return Arrays.asList(
                "not ok",
                "",
                "# OK",
                "bla ok",
                "    -OK",
                "---"
        );
    }
}
