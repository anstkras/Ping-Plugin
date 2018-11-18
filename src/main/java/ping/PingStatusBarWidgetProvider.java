package ping;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetProvider;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PingStatusBarWidgetProvider implements StatusBarWidgetProvider {
    @Nullable
    @Override
    public StatusBarWidget getWidget(@NotNull Project project) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        return new PingWidget(statusBar);
    }
}
