package intellij.plugin.ping.pingUI;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import intellij.plugin.ping.configurable.PingConfig;
import intellij.plugin.ping.ping.CommandLinePing;
import intellij.plugin.ping.ping.PingComponent;
import intellij.plugin.ping.ping.PingResultListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static intellij.plugin.ping.icons.PingPluginsIcons.*;

class PingWidget implements StatusBarWidget {
    private final PingPresentation pingPresentation;
    private final Logger logger = Logger.getInstance(PingWidget.class);
    private StatusBar statusBar;
    private PingResultListener pingResultListener;

    public PingWidget() {
        this.pingPresentation = new PingPresentation();
    }

    @NotNull
    @Override
    public String ID() {
        return "pingWidget";
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return pingPresentation;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        this.statusBar = statusBar;
        registerCustomListeners();
    }

    @Override
    public void dispose() {
        PingComponent pingComponent = ApplicationManager.getApplication().getComponent(PingComponent.class);
        CommandLinePing commandLinePing = pingComponent.getCommandLinePing();
        commandLinePing.removeListener(pingResultListener);
    }

    private void registerCustomListeners() {
        PingComponent pingComponent = ApplicationManager.getApplication().getComponent(PingComponent.class);
        CommandLinePing commandLinePing = pingComponent.getCommandLinePing();
        pingResultListener = new PingResultListener() {
            @Override
            public void onError(String message) {
                updateIcon(RED_ICON);
            }

            @Override
            public void onMeasuredTime(long time) {
                PingConfig config = PingConfig.getInstance();
                if (config == null) {
                    logger.info("ping config is null");
                } else {
                    long fastTime = config.getFastTime();
                    long mediumTime = config.getMediumTime();
                    if (time <= fastTime) {
                        updateIcon(GREEN_ICON);
                    } else if (time <= mediumTime) {
                        updateIcon(YELLOW_ICON);
                    } else {
                        updateIcon(RED_ICON);
                    }
                }
            }

            @Override
            public void onStop() {
                updateIcon(GRAY_ICON);
            }
        };
        commandLinePing.addListener(pingResultListener);
    }

    private void updateIcon(Icon icon) {
        if (pingPresentation.getIcon() != icon) {
            if (statusBar == null) {
                logger.info("status bar is null");
            } else {
                pingPresentation.setIcon(icon);
                statusBar.updateWidget(ID());
            }
        }
    }
}