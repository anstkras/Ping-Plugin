package ping;

import java.util.EventListener;

interface PingResultListener extends EventListener {
    void onError(String message);

    void onGivenTime(long time);
}
