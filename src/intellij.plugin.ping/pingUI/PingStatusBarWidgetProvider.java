package intellij.plugin.ping.pingUI;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PingStatusBarWidgetProvider implements StatusBarWidgetProvider {
    @Nullable
    @Override
    public StatusBarWidget getWidget(@NotNull Project project) {
        return new PingWidget();
    }
}
