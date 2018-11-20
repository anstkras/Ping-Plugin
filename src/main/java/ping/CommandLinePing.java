package ping;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// CommandLinePing parses ping output to get average round-trip time
public class CommandLinePing implements PingExecutor {
    private final Logger logger = Logger.getLogger("ping");
    private final long period = 30; // in seconds
    private final String internetAddress;
    private final List<PingResultListener> listeners = new ArrayList<>();
    private static final String ERROR_MESSAGE = "There are some problems with internet connection";

    protected CommandLinePing(String internetAddress, PingResultListener pingResultListener) {
        this.internetAddress = internetAddress;
    }

    @Override
    public void start() {
        Runnable runPing = () -> {
            ProcessHandler processHandler = null;
            try {
                processHandler = new OSProcessHandler(new GeneralCommandLine("ping", "-c", "4", internetAddress));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            logger.log(Level.INFO, "start ping process");

            ProcessAdapter processAdapter = new ProcessAdapter() {
                @Override
                public void processTerminated(@NotNull ProcessEvent event) {
                    if (event.getExitCode() != 0) {
                        logger.log(Level.INFO, ERROR_MESSAGE);
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
                            logger.log(Level.INFO, "rtt time: " + String.valueOf(time));
                            for (PingResultListener listener : listeners) {
                                listener.onGivenTime(time);
                            }
                        } else {
                            logger.log(Level.WARNING, "average time has not been found in string: " + str);
                        }
                    }
                }
            };
            processHandler.addProcessListener(processAdapter);
            processHandler.startNotify();
            logger.log(Level.INFO, "end ping process");
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runPing, 0, period, TimeUnit.SECONDS);
    }

    public void addListener(PingResultListener pingResultListener) {
        listeners.add(pingResultListener);
    }

    public void removeListener(PingResultListener pingResultListener) {
        listeners.remove(pingResultListener);
    }
}
