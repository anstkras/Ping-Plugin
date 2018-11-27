package ping;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// CommandLinePing parses ping output to get average round-trip time
public class CommandLinePing implements PingExecutor {
    private static final String ERROR_MESSAGE = "There are some problems with internet connection";
    private final Logger logger = Logger.getInstance(CommandLinePing.class);
    private final List<PingResultListener> listeners = new ArrayList<>();
    private long timeFrequency;
    private TimeUnit timeUnit;
    private String internetAddress;
    private ScheduledExecutorService executor;

    protected CommandLinePing(String internetAddress, long timeFrequency, TimeUnit timeUnit) {
        setParameters(internetAddress, timeFrequency, timeUnit);
    }

    public void setParameters(String internetAddress, long timeFrequency, TimeUnit timeUnit) {
        this.internetAddress = internetAddress;
        this.timeFrequency = timeFrequency;
        this.timeUnit = timeUnit;
    }

    public void restart() {
        executor.shutdown();
        start();
    }

    @Override
    public void start() {
        Runnable runPing = () -> {
            ProcessHandler processHandler = null;
            try {
                processHandler = new OSProcessHandler(new GeneralCommandLine("ping", "-c", "4", internetAddress));
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
            logger.info("start ping process");

            ProcessAdapter processAdapter = new ProcessAdapter() {
                @Override
                public void processTerminated(@NotNull ProcessEvent event) {
                    if (event.getExitCode() != 0) {
                        logger.error(ERROR_MESSAGE);
                        for (PingResultListener listener : listeners) {
                            listener.onError(ERROR_MESSAGE);
                        }
                    }
                }

                @Override
                public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                    String str = event.getText();
                    if (str.startsWith("rtt")) {
                        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");
                        Matcher matcher = pattern.matcher(str);
                        if (matcher.find() && matcher.find()) {
                            long time = (long) Double.parseDouble(matcher.group());
                            logger.info("rtt time: " + String.valueOf(time));
                            for (PingResultListener listener : listeners) {
                                listener.onGivenTime(time);
                            }
                        } else {
                            logger.error("average time has not been found in string: " + str);
                        }
                    }
                }
            };
            processHandler.addProcessListener(processAdapter);
            processHandler.startNotify();
            logger.info("end ping process");
        };
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runPing, 0, timeFrequency, timeUnit);
    }

    public void addListener(PingResultListener pingResultListener) {
        listeners.add(pingResultListener);
    }

    public void removeListener(PingResultListener pingResultListener) {
        listeners.remove(pingResultListener);
    }
}
