package intellij.plugin.ping.ping;

import com.intellij.execution.ExecutionException;
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

// CommandLinePing parses ping output to get average round-trip time
public class CommandLinePing {
    private static final String ERROR_MESSAGE = "There are some problems with internet connection";
    private final static Logger logger = Logger.getInstance(CommandLinePing.class);
    private final List<PingResultListener> listeners = new ArrayList<>();
    private long timeFrequency;
    private TimeUnit timeUnit;
    private String internetAddress;
    private ScheduledExecutorService executor;

    public CommandLinePing() {
    }

    public void setParameters(String internetAddress, long timeFrequency, TimeUnit timeUnit) {
        this.internetAddress = internetAddress;
        this.timeFrequency = timeFrequency;
        this.timeUnit = timeUnit;
    }

    public void setInternetAddress(String internetAddress) {
        this.internetAddress = internetAddress;
    }

    public void restart() {
        stop();
        start();
    }

    public void start() {
        start(true);
    }

    public void stop() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        for (PingResultListener listener : listeners) {
            listener.onStop();
        }
    }

    public void start(boolean repeat) {
        Runnable runPing = () -> {
            PingAdapter pingAdapter = PingAdapter.getPingAdapter();
            ProcessHandler processHandler = null;
            try {
                processHandler = new OSProcessHandler(pingAdapter.getGeneralCommandLine(internetAddress));
            } catch (ExecutionException e) {
                logger.info(e.getMessage());
            }
            logger.info("start ping process");

            StringBuilder stringBuilder = new StringBuilder();
            ProcessAdapter processAdapter = new ProcessAdapter() {
                @Override
                public void processTerminated(@NotNull ProcessEvent event) {
                    String pingOutput = stringBuilder.toString();
                    if (event.getExitCode() != 0) {
                        logger.info(ERROR_MESSAGE);
                        for (PingResultListener listener : listeners) {
                            listener.onError(pingOutput);
                        }
                    } else {
                        try {
                            long time = pingAdapter.timeMeasured(pingOutput);
                            logger.info("rtt time: " + String.valueOf(time));
                            for (PingResultListener listener : listeners) {
                                listener.onMeasuredTime(time);
                            }
                        } catch (Exception e) {
                            logger.info(e.getMessage());
                            for (PingResultListener listener : listeners) {
                                listener.onError(pingOutput);
                            }
                        }
                    }
                }

                @Override
                public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                    String str = event.getText();
                    stringBuilder.append(str);
                }
            };
            processHandler.addProcessListener(processAdapter);
            processHandler.startNotify();
            logger.info("end ping process");
        };
        executor = Executors.newScheduledThreadPool(1);
        if (repeat) {
            executor.scheduleAtFixedRate(runPing, 0, timeFrequency, timeUnit);
        } else {
            executor.execute(runPing);
        }
    }

    public void addListener(PingResultListener pingResultListener) {
        listeners.add(pingResultListener);
    }

    public void removeListener(PingResultListener pingResultListener) {
        listeners.remove(pingResultListener);
    }
}
