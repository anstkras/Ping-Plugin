package intellij.plugin.ping.ping;

import com.intellij.openapi.components.BaseComponent;
import intellij.plugin.ping.configurable.PingConfig;

import java.util.concurrent.TimeUnit;

public class PingComponent implements BaseComponent {
    private final PingConfig config;
    private String internetAddress;
    private long timeFrequency;
    private TimeUnit timeUnit;
    private boolean pluginEnabled;
    private final CommandLinePing commandLinePing;

    PingComponent() {
        config = PingConfig.getInstance();
        commandLinePing = new CommandLinePing();
    }

    @Override
    public void initComponent() {
        getParameters();
        if (pluginEnabled) {
            commandLinePing.setParameters(internetAddress, timeFrequency, timeUnit);
            commandLinePing.start();
        }
    }

    public void updateParameters() {
        getParameters();
        if (pluginEnabled) {
            commandLinePing.setParameters(internetAddress, timeFrequency, timeUnit);
            commandLinePing.restart();
        } else {
            commandLinePing.stop();
        }
    }

    private void getParameters() {
        internetAddress = config.getInternetAddress();
        timeFrequency = config.getTimeFrequency();
        timeUnit = config.getTimeUnit();
        pluginEnabled = config.isEnabled();
    }

    public CommandLinePing getCommandLinePing() {
        return commandLinePing;
    }
}
