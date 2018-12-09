package intellij.plugin.ping.ping;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.SystemInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingAdapter {
    // pattern for 4 floating numbers separating by slashes
    private static final Pattern LINUX_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)");
    private static final Pattern MAC_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)/(\\d+(\\.\\d+)?)");
    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static final Pattern WIN_CHECK_PATTERN = Pattern.compile(".*(" + ipv4Pattern + "|" + ipv6Pattern + ")" + ".*\\d+.*\\d.*\\d");
    private static final Pattern WIN_PATTERN = Pattern.compile("\\d+.*\\d+.*(\\d+)");
    private static final int pingCount = 4;
    private static String stringPingCount = String.valueOf(pingCount);

    public static GeneralCommandLine getGeneralCommandLine(String internetAddress) {
        if (SystemInfo.isLinux) {
            return new GeneralCommandLine("ping", "-c", stringPingCount, internetAddress);
        }

        if (SystemInfo.isMac) {
            return new GeneralCommandLine("ping", "-c", stringPingCount, internetAddress);
        }

        if (SystemInfo.isWindows) {
            return new GeneralCommandLine("ping", "-n", stringPingCount, internetAddress);
        }

        return null;
    }

    public static long timeMeasured(String pingOutput) throws IllegalStateException {
        String[] strings = pingOutput.split("[\r\n]+");
        try {
            if (SystemInfo.isLinux) {
                Matcher matcher = LINUX_PATTERN.matcher(strings[strings.length - 1]);
                if (matcher.group(2) != null) {
                    long time = (long) Double.parseDouble(matcher.group(2));
                    return time;
                }
            }
            if (SystemInfo.isMac) {
                Matcher matcher = MAC_PATTERN.matcher(strings[strings.length - 1]);
                if (matcher.group(2) != null) {
                    long time = (long) Double.parseDouble(matcher.group(2));
                    return time;
                }
            }
            if (SystemInfo.isWindows) {
                int count = 0;
                for (String string : strings) {
                    if (WIN_CHECK_PATTERN.matcher(string).matches()) {
                        count++;
                    }
                }
                if (count < pingCount) {
                    throw new IllegalStateException("average time has not been found in string: " + pingOutput);
                }
                Matcher matcher = WIN_PATTERN.matcher(strings[strings.length - 1]);
                if (matcher.group(3) != null) {
                    long time = (long) Double.parseDouble(matcher.group(3));
                    return time;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("average time has not been found in string: " + pingOutput);
        }
        throw new IllegalStateException("Unsupported OS");
    }
}
