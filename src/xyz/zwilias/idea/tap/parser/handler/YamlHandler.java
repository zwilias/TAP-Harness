package xyz.zwilias.idea.tap.parser.handler;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.zwilias.idea.tap.parser.State;

import java.util.regex.Pattern;

public class YamlHandler implements LineHandler {
    private static final Logger LOG = Logger.getInstance(YamlHandler.class);

    private final State state;
    private static final Pattern START_YAML_BLOCK = Pattern.compile("^\\s*---.*$");
    private static final Pattern END_YAML_BLOCK = Pattern.compile("^\\s*\\.{3}.*$");
    private LineHandler previousHandler;
    private StringBuffer buffer;

    public YamlHandler(State state) {
        this.state = state;
    }

    @Override
    public boolean shouldHandle(@NotNull String line) {
        return state.isInYaml() || START_YAML_BLOCK.matcher(line).matches();

    }

    @Override
    public void handle(@NotNull String line) {
        if (! state.isInYaml() && START_YAML_BLOCK.matcher(line).matches()) {
            state.setInYaml(true);
            this.previousHandler = state.getPreviousHandler();
            buffer = new StringBuffer();
            return;
        }

        if (state.isInYaml() && END_YAML_BLOCK.matcher(line).matches()) {
            state.setInYaml(false);
            if (this.previousHandler != null) {
                this.previousHandler.addDiagnostics(buffer.toString());
            } else {
                LOG.warn("Tried to add diagnostics without previous handler...");
            }
            return;
        }

        buffer.append(line).append("\n");
    }

    @Override
    public void addDiagnostics(String diagnostics) {
        LOG.error("Trying to add diagnostics to self...");
    }

    @Override
    public void finish() {
        if (this.previousHandler != null) {
            this.previousHandler.finish();
        } else {
            LOG.warn("Trying to finish without previous handler...");
        }
    }
}
