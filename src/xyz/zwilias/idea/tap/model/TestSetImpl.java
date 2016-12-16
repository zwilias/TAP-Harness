package xyz.zwilias.idea.tap.model;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TestSetImpl implements TestSet<TestResultImpl>, RootNode, TestSuiteCreator {
    private final static Logger LOG = Logger.getInstance(TestSetImpl.class);

    private final List<TestResultImpl> testResults = new LinkedList<>();
    private final List<TestResultListener<TestResultImpl>> testResultListeners = new LinkedList<>();
    private final List<SuiteResultListener> suiteResultListeners = new LinkedList<>();

    private final Map<String, TestSuiteImpl> suites = new HashMap<>();

    @Override
    public void addTestResult(@NotNull TestResultImpl testResult) {
        testResults.add(testResult);

        String description = testResult.getDescription();
        Node parent = this;

        if (description != null) {
            LinkedList<String> parts = new LinkedList<>(Arrays.asList(description.split("›")));

            String lastPart = parts.removeLast();
            testResult.setDescription(lastPart);

            TestSuiteCreator suiteCreator = this;

            while (parts.size() > 0) {
                String part = parts.remove(0);

                TestSuiteImpl suite = suiteCreator.getOrCreateSuite(part.trim());
                parent = suite;
                suiteCreator = suite;
            }
        }

        testResult.setParent(parent);

        this.testResultListeners.forEach(testResultListener -> testResultListener.onTestResultAdded(testResult));
    }

    public TestSuiteImpl getOrCreateSuite(String name) {
        if (!suites.containsKey(name)) {
            TestSuiteImpl suite = new TestSuiteImpl(this, name);
            suites.put(name, suite);
            this.testSuiteCreated(suite);
        }

        return this.suites.get(name);
    }

    @Override
    public void addTestResultListener(TestResultListener<TestResultImpl> testResultListener) {
        testResultListeners.add(testResultListener);
    }

    public void addSuiteResultListener(SuiteResultListener suiteResultListener) {
        suiteResultListeners.add(suiteResultListener);
    }

    @NotNull
    @Override
    public List<TestResultImpl> getTestResults() {
        return testResults;
    }

    @Override
    public void testSuiteCreated(TestSuiteImpl testSuite) {
        this.suiteResultListeners.forEach(suiteResultListener -> suiteResultListener.onSuiteAdded(testSuite));
    }
}
