package intellij.plugin.ping.pingUI;

import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static intellij.plugin.ping.icons.PingPluginsIcons.GRAY_ICON;

public class PingPresentation implements StatusBarWidget.IconPresentation {
    private final PingWidget widget;
    private Icon icon = GRAY_ICON;

    public PingPresentation(PingWidget pingWidget) {
        this.widget = pingWidget;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return "Ping Plugin";
    }

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return mouseEvent -> {
            if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
            }
        };
    }
}