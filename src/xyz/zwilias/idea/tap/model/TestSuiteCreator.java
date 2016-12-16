package xyz.zwilias.idea.tap.model;

public interface TestSuiteCreator extends Node {
    void testSuiteCreated(TestSuiteImpl testSuite);
    TestSuiteImpl getOrCreateSuite(String name);
}
