package ping;

import java.util.EventListener;

abstract class PingListener implements EventListener {
    private final long fastTime; // in ms
    private final long mediumTime; // in ms

    PingListener(long fastTime, long mediumTime) {
        this.fastTime = fastTime;
        this.mediumTime = mediumTime;
    }

    abstract public void onError();

    abstract public void onFastTime();

    abstract public void onMediumTime();

    abstract public void onSlowTime();

    public long getFastTime() {
        return fastTime;
    }

    public long getMediumTime() {
        return mediumTime;
    }

}
