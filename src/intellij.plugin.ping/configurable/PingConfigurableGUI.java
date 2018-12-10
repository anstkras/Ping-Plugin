package intellij.plugin.ping.configurable;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.fields.IntegerField;
import intellij.plugin.ping.ping.CommandLinePing;
import intellij.plugin.ping.ping.PingComponent;
import intellij.plugin.ping.ping.PingResultListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

class PingConfigurableGUI {
    private JPanel rootPanel;
    private JTextField addressTextField;
    private IntegerField fastTimeField;
    private IntegerField mediumTimeField;
    private IntegerField frequencyField;
    private JComboBox<TimeUnitRecord> timeUnitComboBox;
    private JButton testButton;
    private JTextArea testMessage;
    private JCheckBox enabledCheckBox;
    private PingConfig config;

    public PingConfigurableGUI() {
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

        testButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enabledCheckBox.isSelected()) {
                    super.mouseClicked(e);
                    setAllEnabled(false);
                    testInternetAddress();
                }
            }
        });

        enabledCheckBox.setSelected(config.isEnabled());
        if (!config.isEnabled()) {
            setAllEnabled(false);
            enabledCheckBox.setEnabled(true);
        }

        enabledCheckBox.addActionListener(e -> {
            setAllEnabled(enabledCheckBox.isSelected());
            enabledCheckBox.setEnabled(true);
        });
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
        modified |= enabledCheckBox.isSelected() != config.isEnabled();
        return modified;
    }

    public void apply() throws ConfigurationException {
        config.setEnabled(enabledCheckBox.isSelected());
        if (enabledCheckBox.isSelected()) {
            testTimes();

            config.setInternetAddress(addressTextField.getText());
            config.setFastTime(Integer.valueOf(fastTimeField.getText()));
            config.setMediumTime(Integer.valueOf(mediumTimeField.getText()));
            config.setTimeFrequency(Integer.valueOf(frequencyField.getText()));
            config.setTimeUnit(((TimeUnitRecord) timeUnitComboBox.getSelectedItem()).getTimeUnit());
        }

        ApplicationManager.getApplication().getComponent(PingComponent.class).updateParameters();
    }

    public void reset() {
        addressTextField.setText(config.getInternetAddress());
        fastTimeField.setText(String.valueOf(config.getFastTime()));
        mediumTimeField.setText(String.valueOf(config.getMediumTime()));
        frequencyField.setText(String.valueOf(config.getTimeFrequency()));
        timeUnitComboBox.setSelectedItem(new TimeUnitRecord(config.getTimeUnit()));
        enabledCheckBox.setSelected(config.isEnabled());
    }

    private void testInternetAddress() {
        CommandLinePing commandLinePing = new CommandLinePing();
        commandLinePing.setInternetAddress(addressTextField.getText());
        commandLinePing.start(false);
        commandLinePing.addListener(new PingResultListener() {
            @Override
            public void onError(String message) {
                testMessage.setForeground(JBColor.RED);
                testMessage.setVisible(true);
                testMessage.setText(message);
                setAllEnabled(true);
            }

            @Override
            public void onMeasuredTime(long time) {
                testMessage.setForeground(JBColor.BLACK);
                testMessage.setVisible(true);
                testMessage.setText("Internet address is valid");
                setAllEnabled(true);
            }

            @Override
            public void onStop() {
            }
        });
    }

    private void testTimes() throws ConfigurationException {
        fastTimeField.validateContent();
        mediumTimeField.validateContent();
        frequencyField.validateContent();
        if (Long.valueOf(mediumTimeField.getText()) < Long.valueOf(fastTimeField.getText())) {
            throw new ConfigurationException("Fast time should be less than medium time");
        }
    }

    private void setAllEnabled(boolean enabled) {
        Component[] components = rootPanel.getComponents();
        for (Component component : components) {
            component.setEnabled(enabled);
        }
    }
}
