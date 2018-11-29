package intellij.plugin.ping.configurable;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

@State(
        name = "PingConfig",
        storages = {
                @Storage("other.xml")}
)
public class PingConfig implements PersistentStateComponent<PingConfig> {
    // TODO is this a valid way to set default values?
    private String internetAddress = "google.com";
    private long fastTime = 20;
    private long mediumTime = 40;
    private long timeFrequency = 30;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private boolean isEnabled = false;

    @Nullable
    public static PingConfig getInstance() {
        return ServiceManager.getService(PingConfig.class);
    }

    @Nullable
    @Override
    public PingConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PingConfig pingConfig) {
        XmlSerializerUtil.copyBean(pingConfig, this);
    }

    public String getInternetAddress() {
        return internetAddress;
    }

    public void setInternetAddress(String internetAddress) {
        this.internetAddress = internetAddress;
    }

    public long getFastTime() {
        return fastTime;
    }

    public void setFastTime(long fastTime) {
        this.fastTime = fastTime;
    }

    public long getMediumTime() {
        return mediumTime;
    }

    public void setMediumTime(long mediumTime) {
        this.mediumTime = mediumTime;
    }

    public long getTimeFrequency() {
        return timeFrequency;
    }

    public void setTimeFrequency(long timeFrequency) {
        this.timeFrequency = timeFrequency;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
