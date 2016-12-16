package xyz.zwilias.idea.tap.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.zwilias.idea.tap.runner.TapRunProfileState;


public class TapRunConfiguration extends RunConfigurationBase  {
    private static final Logger LOG = Logger.getInstance(TapRunConfiguration.class);
    private String command;

    TapRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    @NotNull
    @Override
    public SettingsEditor<TapRunConfiguration> getConfigurationEditor() {
        return new TapConfigurationEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new TapRunProfileState(this);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);

        this.command = element.getAttributeValue("command");
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);

        if (this.command != null && !this.command.isEmpty()) {
            element.setAttribute("command", this.command);
        } else {
            LOG.info("Command is null");
        }
    }
}
