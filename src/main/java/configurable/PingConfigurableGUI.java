package configurable;

import javax.swing.*;

public class PingConfigurableGUI {
    private JTextField addressTextField;
    private JPanel rootPanel;
    private JTextField fastTimeTextField;
    private JTextField mediumTimeTextField;

    public JPanel getRootPanel() {
        return rootPanel;
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
}
