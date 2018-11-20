package ping;

import java.util.EventListener;

public interface PingComponentListener extends EventListener {
    void onError(String message);

    void onFastTime();

    void onMediumTime();

    void onSlowTime();
}
