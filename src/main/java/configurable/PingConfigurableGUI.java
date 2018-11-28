package configurable;

import com.intellij.openapi.application.ApplicationManager;
import ping.PingComponent;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class PingConfigurableGUI {
    private JPanel rootPanel;
    private JTextField addressTextField;
    private JTextField fastTimeTextField;
    private JTextField mediumTimeTextField;
    private JTextField frequencyTextField;
    private JComboBox<TimeUnitRecord> timeUnitComboBox;
    private JButton testButton;
    private JLabel testMessage;
    private PingConfig config;

    public PingConfigurableGUI() {
        testButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                test();
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void createUI() {
        config = PingConfig.getInstance();
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
        frequencyTextField.setText(String.valueOf(config.getTimeFrequency()));
        TimeUnitRecord[] timeUnitRecords = {new TimeUnitRecord(TimeUnit.MILLISECONDS), new TimeUnitRecord(TimeUnit.SECONDS), new TimeUnitRecord((TimeUnit.MINUTES))};
        ComboBoxModel<TimeUnitRecord> comboBoxModel = new DefaultComboBoxModel<>(timeUnitRecords);
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
        modified |= ((TimeUnitRecord) timeUnitComboBox.getSelectedItem()).getTimeUnit() != config.getTimeUnit();
        return modified;
    }

    public void apply() {
        if (!test()) {
            return;
        }
        config.setInternetAddress(addressTextField.getText());
        config.setFastTime(Integer.valueOf(fastTimeTextField.getText()));
        config.setMediumTime(Integer.valueOf(mediumTimeTextField.getText()));
        config.setTimeFrequency(Integer.valueOf(frequencyTextField.getText()));
        config.setTimeUnit(((TimeUnitRecord) timeUnitComboBox.getSelectedItem()).getTimeUnit());
        ApplicationManager.getApplication().getComponent(PingComponent.class).updateParameters();
    }

    public void reset() {
        addressTextField.setText(config.getInternetAddress());
        fastTimeTextField.setText(String.valueOf(config.getFastTime()));
        mediumTimeTextField.setText(String.valueOf(config.getMediumTime()));
        frequencyTextField.setText(String.valueOf(config.getTimeFrequency()));
        timeUnitComboBox.setSelectedItem(new TimeUnitRecord(config.getTimeUnit()));
    }

    private Boolean test() {
        // TODO add ip validators
        if (Long.valueOf(mediumTimeTextField.getText()) < Long.valueOf(fastTimeTextField.getText())) {
            testMessage.setText("Fast time should be less than medium time");
            return false;
        }
        testMessage.setText("Everything is correct");
        return true;
    }
}
