package configurable;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.fields.IntegerField;
import ping.PingComponent;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class PingConfigurableGUI {
    private JPanel rootPanel;
    private JTextField addressTextField;
    private IntegerField fastTimeField;
    private IntegerField mediumTimeField;
    private IntegerField frequencyField;
    private JComboBox<TimeUnitRecord> timeUnitComboBox;
    private JButton testButton;
    private JLabel testMessage;
    private PingConfig config;

    public PingConfigurableGUI() {
        testButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //testTimes();
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void createUI() {
        config = PingConfig.getInstance();
        addressTextField.setText(config.getInternetAddress());

        fastTimeField.setCanBeEmpty(false);
        fastTimeField.setMinValue(1);
        fastTimeField.setMaxValue(3000);
        fastTimeField.setText(String.valueOf(config.getFastTime()));

        mediumTimeField.setCanBeEmpty(false);
        mediumTimeField.setMinValue(1);
        mediumTimeField.setMaxValue(3000);
        mediumTimeField.setText(String.valueOf(config.getMediumTime()));

        frequencyField.setCanBeEmpty(false);
        frequencyField.setMinValue(1);
        frequencyField.setMaxValue(30000);
        frequencyField.setText(String.valueOf(config.getTimeFrequency()));

        TimeUnitRecord[] timeUnitRecords = {new TimeUnitRecord(TimeUnit.MILLISECONDS), new TimeUnitRecord(TimeUnit.SECONDS), new TimeUnitRecord((TimeUnit.MINUTES))};
        ComboBoxModel<TimeUnitRecord> comboBoxModel = new DefaultComboBoxModel<>(timeUnitRecords);
        timeUnitComboBox.setModel(comboBoxModel);
        timeUnitComboBox.setSelectedItem(new TimeUnitRecord(config.getTimeUnit()));
    }

    public JTextField getMediumTimeField() {
        return mediumTimeField;
    }

    public JTextField getFastTimeField() {
        return fastTimeField;
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public JTextField getFrequencyField() {
        return frequencyField;
    }

    public JComboBox getTimeUnitComboBox() {
        return timeUnitComboBox;
    }

    public boolean isModified() {
        boolean modified = !addressTextField.getText().equals(config.getInternetAddress());
        modified |= !fastTimeField.getText().equals(String.valueOf(config.getFastTime()));
        modified |= !mediumTimeField.getText().equals(String.valueOf(config.getMediumTime()));
        modified |= !frequencyField.getText().equals(String.valueOf(config.getTimeFrequency()));
        modified |= ((TimeUnitRecord) timeUnitComboBox.getSelectedItem()).getTimeUnit() != config.getTimeUnit();
        return modified;
    }

    public void apply() throws ConfigurationException {
        testTimes();
        config.setInternetAddress(addressTextField.getText());
        config.setFastTime(Integer.valueOf(fastTimeField.getText()));
        config.setMediumTime(Integer.valueOf(mediumTimeField.getText()));
        config.setTimeFrequency(Integer.valueOf(frequencyField.getText()));
        config.setTimeUnit(((TimeUnitRecord) timeUnitComboBox.getSelectedItem()).getTimeUnit());

        ApplicationManager.getApplication().getComponent(PingComponent.class).updateParameters();
    }

    public void reset() {
        addressTextField.setText(config.getInternetAddress());
        fastTimeField.setText(String.valueOf(config.getFastTime()));
        mediumTimeField.setText(String.valueOf(config.getMediumTime()));
        frequencyField.setText(String.valueOf(config.getTimeFrequency()));
        timeUnitComboBox.setSelectedItem(new TimeUnitRecord(config.getTimeUnit()));
    }

    private void testTimes() throws ConfigurationException {
        fastTimeField.validateContent();
        mediumTimeField.validateContent();
        frequencyField.validateContent();
        if (Long.valueOf(mediumTimeField.getText()) < Long.valueOf(fastTimeField.getText())) {
            throw new ConfigurationException("Fast time should be less than medium time");
        }

        testMessage.setText("Everything is correct");
    }
}
