package intellij.plugin.ping.ping;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.SystemInfo;

abstract class PingAdapter {
    protected static final String NO_TIME_ERROR = "average time has not been found in string: ";
    protected static final int PING_COUNT = 4;
    protected static final String STRING_PING_COUNT = String.valueOf(PING_COUNT);

    public static PingAdapter getPingAdapter() throws IllegalStateException {
        if (SystemInfo.isLinux) {
            return new PingLinuxAdapter();
        }
        if (SystemInfo.isWindows) {
            return new PingWinAdapter();
        }
        if (SystemInfo.isMac) {
            return new PingMacAdapter();
        }
        throw new IllegalStateException("unsupported OS");
    }

    abstract GeneralCommandLine getGeneralCommandLine(String internetAddress);

    abstract long timeMeasured(String pingOutput) throws IllegalStateException;
}
