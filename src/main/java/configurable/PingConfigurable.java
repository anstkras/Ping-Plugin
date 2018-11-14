package configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This ProjectConfigurable class appears on Settings dialog,
 * to let user to configure this plugin's behavior.
 */
public class PingConfigurable implements SearchableConfigurable {

    PingConfigurableGUI gui;

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
        return gui.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }
}