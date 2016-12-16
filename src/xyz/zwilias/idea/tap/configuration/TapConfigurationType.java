package xyz.zwilias.idea.tap.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import icons.TapIcons;
import org.jetbrains.annotations.NotNull;

public class TapConfigurationType extends ConfigurationTypeBase {
    protected TapConfigurationType() {
        super("TapHarness", "TAP Harness", "TAP Harness", TapIcons.Tap);

        addFactory(new ConfigurationFactory(this) {
            @NotNull
            @Override
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                return new TapRunConfiguration(project, this, "TAP Harness");
            }

            @Override
            public boolean isConfigurationSingletonByDefault() {
                return true;
            }

            @Override
            public boolean canConfigurationBeSingleton() {
                return false;
            }
        });
    }

    @NotNull
    public static TapConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(TapConfigurationType.class);
    }

    @NotNull
    public static ConfigurationFactory getFactory() {
        TapConfigurationType type = getInstance();
        return type.getConfigurationFactories()[0];
    }
}
