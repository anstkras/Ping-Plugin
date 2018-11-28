package configurable;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TimeUnitRecord {
    private final TimeUnit timeUnit;

    public TimeUnitRecord(@NotNull TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String toString() {
        return timeUnit.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object record) {
        if (record == null) {
            return false;
        }
        if (!(record instanceof TimeUnitRecord)) {
            return false;
        }
        return timeUnit == ((TimeUnitRecord) record).timeUnit;
    }
}
