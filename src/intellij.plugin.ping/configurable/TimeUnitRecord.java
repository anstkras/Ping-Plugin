package intellij.plugin.ping.configurable;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

class TimeUnitRecord {
    private final TimeUnit timeUnit;

    TimeUnitRecord(@NotNull TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    TimeUnit getTimeUnit() {
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
