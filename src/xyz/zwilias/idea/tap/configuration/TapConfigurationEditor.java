package xyz.zwilias.idea.tap.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.StatusText;
import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.TapBundle;

import javax.swing.*;

public class TapConfigurationEditor extends SettingsEditor<TapRunConfiguration> {
    private JComponent component;
    private final JBTextField commandField;

    TapConfigurationEditor() {
        commandField = getCommandField();
        component = new FormBuilder()
                .addLabeledComponent(TapBundle.message("runConfiguration.command.label"), commandField)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull TapRunConfiguration tapRunConfiguration) {
        commandField.setText(tapRunConfiguration.getCommand());
    }

    @Override
    protected void applyEditorTo(@NotNull TapRunConfiguration tapRunConfiguration) throws ConfigurationException {
        tapRunConfiguration.setCommand(commandField.getText());
    }

    @NotNull
    private static JBTextField getCommandField() {
        JBTextField commandField = new JBTextField();
        StatusText emptyStatusText = commandField.getEmptyText();
        emptyStatusText.setText("TAP producing commandField (e.g. ava --tap)");
        return commandField;
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return component;
    }
}
