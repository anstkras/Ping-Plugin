package ping;

abstract public class PingExecutor {
    protected final PingListener pingListener;

    protected PingExecutor(PingListener pingListener) {
        this.pingListener = pingListener;
    }

    abstract public void start();
}
