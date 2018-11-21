package ping;

import com.intellij.openapi.components.BaseComponent;
import configurable.PingConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PingComponent implements BaseComponent {
    private final PingConfig config;
    private final List<PingComponentListener> listeners = new ArrayList<>();
    private String internetAddress;
    private long fastTime;
    private long mediumTime;
    private long timeFrequency;
    private TimeUnit timeUnit;
    private CommandLinePing commandLinePing;
    private PingResultListener pingResultListener;

    PingComponent() {
        config = PingConfig.getInstance();
    }

    @Override
    public void initComponent() {
        getParameters();
        commandLinePing = new CommandLinePing(internetAddress, timeFrequency, timeUnit);
        pingResultListener = getListener();
        commandLinePing.addListener(pingResultListener);
        commandLinePing.start();
    }

    public void updateParameters() {
        commandLinePing.removeListener(pingResultListener);
        getParameters();
        commandLinePing.setParameters(internetAddress, timeFrequency, timeUnit);
        pingResultListener = getListener();
        commandLinePing.addListener(pingResultListener);
        commandLinePing.restart();
    }

    public void addListener(PingComponentListener pingListener) {
        listeners.add(pingListener);
    }

    public void removeListener(PingComponentListener pingListener) {
        listeners.remove(pingListener);
    }

    private void getParameters() {
        internetAddress = config.getInternetAddress();
        fastTime = config.getFastTime();
        mediumTime = config.getMediumTime();
        timeFrequency = config.getTimeFrequency();
        timeUnit = config.getTimeUnit();
    }

    private PingResultListener getListener() {
        return new PingResultListener() {
            @Override
            public void onError(String message) {
                for (PingComponentListener listener : listeners) {
                    listener.onError(message);
                }
            }

            @Override
            public void onGivenTime(long time) {
                for (PingComponentListener listener : listeners) {
                    if (time <= fastTime) {
                        listener.onFastTime();
                    } else if (time <= mediumTime) {
                        listener.onMediumTime();
                    } else {
                        listener.onSlowTime();
                    }
                }
            }
        };
    }
}
