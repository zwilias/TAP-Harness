package xyz.zwilias.idea.tap.configuration;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;

public class TapRunConfigurationProducer extends RunConfigurationProducer<TapRunConfiguration> {
    public TapRunConfigurationProducer() {
        super(TapConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(TapRunConfiguration tapRunConfiguration, ConfigurationContext configurationContext, Ref<PsiElement> ref) {
        return false;
    }

    @Override
    public boolean isConfigurationFromContext(TapRunConfiguration tapRunConfiguration, ConfigurationContext configurationContext) {
        return false;
    }
}
