package xyz.zwilias.idea.tap.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestResult {
    @NotNull
    Status getStatus();

    @NotNull
    Integer getTestNumber();

    @Nullable
    String getDescription();


    enum Status {
        OK, NOK;

        /**
         * @param status String representation of the TestResult status: ok or not ok. (Case insensitive)
         * @return Status
         */
        @NotNull
        public static Status create(String status) {
            if ("ok".equalsIgnoreCase(status)) {
                return OK;
            }

            if ("not ok".equalsIgnoreCase(status)) {
                return NOK;
            }

            throw new IllegalArgumentException();
        }
    }
}
