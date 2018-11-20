package ping;

import com.intellij.openapi.components.BaseComponent;
import configurable.PingConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingComponent implements BaseComponent {
    private final Logger logger = Logger.getLogger("ping");
    private final PingConfig config;
    private final List<PingComponentListener> listeners = new ArrayList<>();
    private String internetAddress;
    private long fastTime;
    private long mediumTime;

    PingComponent() {
        logger.log(Level.INFO, "start");
        config = PingConfig.getInstance();
    }

    @Override
    public void initComponent() {
        internetAddress = config.getInternetAddress();
        fastTime = config.getFastTime();
        mediumTime = config.getMediumTime();

        PingResultListener pingResultListener = new PingResultListener() {
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

        CommandLinePing ping = new CommandLinePing(internetAddress, pingResultListener);
        ping.addListener(pingResultListener);
        ping.start();
    }

    public void addListener(PingComponentListener pingListener) {
        listeners.add(pingListener);
    }

    public void removeListener(PingComponentListener pingListener) {
        listeners.remove(pingListener);
    }
}
