package PingUI;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ping.PingComponent;
import ping.PingComponentListener;

import javax.swing.*;

import static icons.PingPluginsIcons.*;

public class PingWidget implements StatusBarWidget {
    private final PingPresentation pingPresentation;
    private final Logger logger = Logger.getInstance(PingWidget.class);
    private StatusBar statusBar;

    public PingWidget() {
        this.pingPresentation = new PingPresentation(this);
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

    }

    public void registerCustomListeners() {
        PingComponent pingComponent = ApplicationManager.getApplication().getComponent(PingComponent.class);
        pingComponent.addListener(new PingComponentListener() {
            @Override
            public void onError(String message) {
                updateIcon(RED_ICON);
            }

            @Override
            public void onFastTime() {
                updateIcon(GREEN_ICON);
            }

            @Override
            public void onMediumTime() {
                updateIcon(YELLOW_ICON);
            }

            @Override
            public void onSlowTime() {
                updateIcon(RED_ICON);
            }
        });
    }

    private void updateIcon(Icon icon) {
        if (pingPresentation.getIcon() != icon) {
            if (statusBar == null) {
                logger.error("status bar is null");
            } else {
                pingPresentation.setIcon(icon);
                statusBar.updateWidget(ID());
            }
        }
    }
}