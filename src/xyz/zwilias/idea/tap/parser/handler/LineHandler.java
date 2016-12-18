package xyz.zwilias.idea.tap.parser.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LineHandler {
    boolean shouldHandle(@NotNull String line);
    void handle(@NotNull String line);
    void addDiagnostics(Object diagnostics);
    void finish();
}
