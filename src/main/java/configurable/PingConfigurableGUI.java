package configurable;

import com.intellij.openapi.project.Project;

import javax.swing.*;

public class PingConfigurableGUI {
    private JPanel rootPanel;
    private JTextField addressTextField;
    private JTextField fastTimeTextField;
    private JTextField mediumTimeTextField;
    private PingConfig config;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void createUI() {
        config = PingConfig.getInstance();
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
    }

    public JTextField getMediumTimeTextField() {
        return mediumTimeTextField;
    }

    public JTextField getFastTimeTextField() {
        return fastTimeTextField;
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public boolean isModified() {
        boolean modified = !addressTextField.getText().equals(config.getInternetAddress());
        modified |= !fastTimeTextField.getText().equals(String.valueOf(config.getFastTime()));
        modified |= !mediumTimeTextField.getText().equals(String.valueOf(config.getMediumTime()));
        return modified;
    }

    public void apply() {
        config.setInternetAddress(addressTextField.getText());
        config.setFastTime(Integer.valueOf(fastTimeTextField.getText()));
        config.setMediumTime(Integer.valueOf(mediumTimeTextField.getText()));
    }

    public void reset() {
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
    }
}
