package intellij.plugin.ping.ping;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PingLinuxAdapter extends PingAdapter {
    private static final Pattern LINUX_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)\\/(\\d+(\\.\\d+)?)\\/(\\d+(\\.\\d+)?)\\/(\\d+(\\.\\d+)?)");

    @Override
    GeneralCommandLine getGeneralCommandLine(String internetAddress) {
        return new GeneralCommandLine("ping", "-c", STRING_PING_COUNT, internetAddress);
    }

    @Override
    long timeMeasured(String pingOutput) throws IllegalStateException {
        String[] strings = pingOutput.split("[\r\n]+");
        if (strings.length < 1) {
            throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
        }

        try {
            Matcher matcher = LINUX_PATTERN.matcher(strings[strings.length - 1]);
            if (matcher.find() && matcher.group(3) != null) {
                long time = (long) Double.parseDouble(matcher.group(3));
                return time;
            } else {
                throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
            }

        } catch (Exception e) {
            throw new IllegalStateException(NO_TIME_ERROR + pingOutput);
        }
    }
}
