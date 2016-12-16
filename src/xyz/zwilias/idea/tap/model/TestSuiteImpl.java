package xyz.zwilias.idea.tap.model;

import java.util.HashMap;
import java.util.Map;

public class TestSuiteImpl implements Node, TestSuiteCreator {
    private final String name;
    private TestSuiteCreator parent;
    private final Map<String, TestSuiteImpl> children = new HashMap<>();

    TestSuiteImpl(TestSuiteCreator parentNode, String name) {
        this.setParent(parentNode);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getNodeId() {
        return this.getParentNodeId().concat("- ").concat(name);
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        if (parent instanceof TestSuiteCreator) {
            this.parent = (TestSuiteCreator) parent;
        }
    }

    @Override
    public void testSuiteCreated(TestSuiteImpl testSuite) {
        this.parent.testSuiteCreated(testSuite);
    }

    @Override
    public TestSuiteImpl getOrCreateSuite(String name) {
        if (!children.containsKey(name)) {
            TestSuiteImpl suite = new TestSuiteImpl(this, name);
            this.testSuiteCreated(suite);
            children.put(name, suite);
        }

        return children.get(name);
    }
}
