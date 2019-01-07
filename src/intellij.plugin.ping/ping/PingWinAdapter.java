package intellij.plugin.ping.ping;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingWinAdapter extends PingAdapter {
    private static final String IPV_4_PATTERN = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String IPV_6_PATTERN = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static final Pattern WIN_CHECK_PATTERN = Pattern.compile(".*(" + IPV_4_PATTERN + "|" + IPV_6_PATTERN + ")" + "[^\\d]*\\d+[^\\d]*\\d+[^\\d]*\\d+");
    private static final Pattern WIN_PATTERN = Pattern.compile("\\d+[^\\d]*\\d+[^\\d]*(\\d+)[^\\d]+");

    @Override
    GeneralCommandLine getGeneralCommandLine(String internetAddress) {
        return new GeneralCommandLine("ping", "-n", STRING_PING_COUNT, internetAddress);
    }

    @Override
    long timeMeasured(String pingOutput) throws IllegalStateException {
        String[] strings = pingOutput.split("[\r\n]+");
        if (strings.length < 1) {
            throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
        }

        try {
            int count = 0;
            for (String string : strings) {
                if (WIN_CHECK_PATTERN.matcher(string).matches()) {
                    count++;
                }
            }
            if (count != PING_COUNT) {
                throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
            }
            Matcher matcher = WIN_PATTERN.matcher(strings[strings.length - 1]);
            if (matcher.find() && matcher.group(1) != null) {
                long time = (long) Double.parseDouble(matcher.group(1));
                return time;
            } else {
                throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
            }

        } catch (Exception e) {
            throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
        }
    }
}

