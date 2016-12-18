package xyz.zwilias.idea.tap.parser;

import xyz.zwilias.idea.tap.parser.handler.LineHandler;

public class State {
    private boolean isInYaml = false;
    private boolean hasPlan = false;
    private boolean hasTests = false;
    private boolean hasBailout = false;
    private LineHandler previousHandler;

    public boolean isInYaml() {
        return isInYaml;
    }

    public void setInYaml(boolean inYaml) {
        isInYaml = inYaml;
    }

    public boolean hasPlan() {
        return hasPlan;
    }

    public void setHasPlan(boolean hasPlan) {
        this.hasPlan = hasPlan;
    }

    public boolean hasTests() {
        return hasTests;
    }

    public void setHasTests(boolean hasTests) {
        this.hasTests = hasTests;
    }

    public boolean hasBailout() {
        return hasBailout;
    }

    public void setHasBailout(boolean hasBailout) {
        this.hasBailout = hasBailout;
    }

    public void finishPreviousHandler() {
        if (this.previousHandler != null) {
            this.previousHandler.finish();
        }
    }

    public LineHandler getPreviousHandler() {
        return this.previousHandler;
    }

    public void setPreviousHandler(LineHandler handler) {
        this.previousHandler = handler;
    }
}
