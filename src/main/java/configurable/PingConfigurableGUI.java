package configurable;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class PingConfigurableGUI {
    private JPanel rootPanel;
    private JTextField addressTextField;
    private JTextField fastTimeTextField;
    private JTextField mediumTimeTextField;
    private JTextField frequencyTextField;
    private JComboBox timeUnitComboBox;
    private PingConfig config;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void createUI() {
        config = PingConfig.getInstance();
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
        frequencyTextField.setText(String.valueOf(config.getTimeFrequency()));
        String[] timeUnits = {"milliseconds", "seconds", "minutes", "hours"};
        ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(timeUnits);
        timeUnitComboBox.setModel(comboBoxModel);
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

    public JTextField getFrequencyTextField() {
        return frequencyTextField;
    }

    public JComboBox getTimeUnitComboBox() {
        return timeUnitComboBox;
    }

    public boolean isModified() {
        boolean modified = !addressTextField.getText().equals(config.getInternetAddress());
        modified |= !fastTimeTextField.getText().equals(String.valueOf(config.getFastTime()));
        modified |= !mediumTimeTextField.getText().equals(String.valueOf(config.getMediumTime()));
        modified |= !frequencyTextField.getText().equals(String.valueOf(config.getTimeFrequency()));
        modified |= !timeUnitComboBox.getSelectedItem().equals(config.getTimeUnit().toString().toLowerCase());
        return modified;
    }

    public void apply() {
        config.setInternetAddress(addressTextField.getText());
        config.setFastTime(Integer.valueOf(fastTimeTextField.getText()));
        config.setMediumTime(Integer.valueOf(mediumTimeTextField.getText()));
        config.setTimeFrequency(Integer.valueOf(frequencyTextField.getText()));
        config.setTimeUnit(TimeUnit.valueOf(((String) timeUnitComboBox.getSelectedItem()).toUpperCase()));
    }

    public void reset() {
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
        frequencyTextField.setText(String.valueOf(config.getTimeFrequency()));
        timeUnitComboBox.setSelectedItem(config.getTimeUnit().toString().toLowerCase());
    }
}
