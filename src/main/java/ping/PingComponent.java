package ping;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static icons.PingPluginsIcons.GREEN_ICON;
import static icons.PingPluginsIcons.RED_ICON;
import static icons.PingPluginsIcons.YELLOW_ICON;

public class PingComponent implements ProjectComponent {
    private final Project project;
    private final Logger logger = Logger.getLogger("ping");
    private PingWidget widget = null;
    private long greenDuration = 10;
    private long yellowDuration = 20;
    private StatusBar statusBar = null;

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

        Runnable runPing = () -> {
            try {
                // TODO count time
                ProcessHandler processHandler = new OSProcessHandler(new GeneralCommandLine("ping", "-w", "1", "google.com"));
                logger.log(Level.INFO, "start ping process");
                ProcessAdapter processAdapter = new ProcessAdapter() {
                    @Override
                    public void processTerminated(@NotNull ProcessEvent event) {
                        updateWidget(event.getExitCode(), 10);
                    }
                };
                processHandler.addProcessListener(processAdapter);
                processHandler.startNotify();
                logger.log(Level.INFO, "end ping process");
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runPing, 0, 2, TimeUnit.SECONDS);
    }

    private void updateWidget(int exitCode, long duration) {
        if (exitCode != 0 || duration > yellowDuration) {
            widget.updateIcon(RED_ICON);
            SwingUtilities.invokeLater(() -> Notifications.Bus.notify(new Notification("ping", "ping", "connection is bad: " + duration + "ms", NotificationType.INFORMATION)));
        }
        if (duration <= yellowDuration && duration > greenDuration) {
            widget.updateIcon(YELLOW_ICON);
            SwingUtilities.invokeLater(() -> Notifications.Bus.notify(new Notification("ping", "ping", "connection is ok: " + duration + "ms", NotificationType.INFORMATION)));
        }
        if (duration <= greenDuration) {
            widget.updateIcon(GREEN_ICON);
            SwingUtilities.invokeLater(() -> Notifications.Bus.notify(new Notification("ping", "ping", "connection is great: " + duration + "ms", NotificationType.INFORMATION)));
        }

    }
}
