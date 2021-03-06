package intellij.plugin.ping.ping;

import java.util.EventListener;

public interface PingResultListener extends EventListener {
    void onError(String message);

    void onMeasuredTime(long time);

    void onStop();
}
