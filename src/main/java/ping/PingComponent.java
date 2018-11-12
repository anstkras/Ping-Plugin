package ping;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import static icons.PingPluginsIcons.*;

public class PingComponent implements ProjectComponent {
    private final Project project;
    private final Logger logger = Logger.getLogger("ping");
    private PingWidget widget = null;
    private StatusBar statusBar = null;
    private long fastTime = 20;
    private long mediumTime = 40;

    PingComponent(Project project) {
        this.project = project;
        logger.log(Level.INFO, "start");
    }

    @Override
    public void projectOpened() {
        statusBar = WindowManager.getInstance().getStatusBar(project);

        if (statusBar == null) {
            logger.log(Level.INFO, "status bar is null");
        } else {
            widget = new PingWidget(statusBar);
            statusBar.addWidget(widget);
            logger.log(Level.INFO, "widget added");
        }

        PingListener pingListener = new PingListener(fastTime, mediumTime) {
            @Override
            public void onError() {
                widget.updateIcon(RED_ICON);
                //SwingUtilities.invokeLater(() -> Notifications.Bus.notify(new Notification("ping", "ping", "connection is bad: " + duration + "ms", NotificationType.INFORMATION)));
            }

            @Override
            public void onFastTime() {
                widget.updateIcon(GREEN_ICON);
            }

            @Override
            public void onMediumTime() {
                widget.updateIcon(YELLOW_ICON);
            }

            @Override
            public void onSlowTime() {
                widget.updateIcon(RED_ICON);
            }
        };

        PingExecutor ping = new CommandLinePing(pingListener);
        ping.start();
    }
}
