package xyz.zwilias.idea.tap.parser;

import xyz.zwilias.idea.tap.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TapParser {
    private final TapFactoryImpl tapFactory;
    private TestSet<TestResultImpl> testSet;

    public TapParser(TestResultListener<TestResultImpl> testResultListener, SuiteResultListener suiteResultListener) {
        this.tapFactory = new TapFactoryImpl();
        testSet = tapFactory.createTestSet();

        testSet.addTestResultListener(testResultListener);
        ((TestSetImpl)testSet).addSuiteResultListener(suiteResultListener);
    }

    private final static Pattern TEST_RESULT_PATTERN = Pattern.compile("\\s*(not ok|NOT OK|ok|OK)\\s+(\\d+)\\s*(?:#|-)?(.*)");

    public void parseLine(String line) {
        Matcher matcher = TEST_RESULT_PATTERN.matcher(line.trim());
        if (matcher.matches()) {
            TestResult.Status status = null;
            String testNumber = "0";

            String statusMatch = matcher.group(1);
            if (statusMatch != null && !"".equals(statusMatch.trim())) {
                status = TestResult.Status.create(statusMatch.trim());
            }

            String testNumberMatch = matcher.group(2);
            if (testNumberMatch != null && !"".equals(testNumberMatch)) {
                testNumber = testNumberMatch.trim();
            }

            String description = matcher.group(3);

            testSet.addTestResult(tapFactory.createTestResult(status, Integer.parseInt(testNumber), description));
        }
    }
}
