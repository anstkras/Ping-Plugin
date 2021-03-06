package intellij.plugin.ping.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PingConfigurable implements SearchableConfigurable {

    private PingConfigurableGUI gui;

    public PingConfigurable() {
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Ping Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.PingConfigurable";
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.PingConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new PingConfigurableGUI();
        gui.createUI();
        return gui.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return gui.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        gui.apply();
    }

    @Override
    public void reset() {
        gui.reset();
    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }
}