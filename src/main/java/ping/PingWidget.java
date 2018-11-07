package ping;

import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PingWidget implements StatusBarWidget {
    private final StatusBar statusBar;
    private final PingPresentation pingPresentation;

    public PingWidget(StatusBar statusBar) {
        this.statusBar = statusBar;
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

    }

    @Override
    public void dispose() {

    }

    public void updateIcon(Icon icon) {
        pingPresentation.setIcon(icon);
        statusBar.updateWidget(ID());
    }
}