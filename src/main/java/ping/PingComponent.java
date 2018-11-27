package ping;

import com.intellij.openapi.components.BaseComponent;
import configurable.PingConfig;

import java.util.concurrent.TimeUnit;

public class PingComponent implements BaseComponent {
    private final PingConfig config;
    private String internetAddress;
    private long timeFrequency;
    private TimeUnit timeUnit;
    private CommandLinePing commandLinePing;

    PingComponent() {
        config = PingConfig.getInstance();
    }

    @Override
    public void initComponent() {
        getParameters();
        commandLinePing = new CommandLinePing(internetAddress, timeFrequency, timeUnit);
        commandLinePing.start();
    }

    public void updateParameters() {
        getParameters();
        commandLinePing.setParameters(internetAddress, timeFrequency, timeUnit);
        commandLinePing.restart();
    }

    private void getParameters() {
        internetAddress = config.getInternetAddress();
        timeFrequency = config.getTimeFrequency();
        timeUnit = config.getTimeUnit();
    }

    public CommandLinePing getCommandLinePing() {
        return commandLinePing;
    }
}
