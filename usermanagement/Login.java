import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;

    public Login() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
}

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        panel.add(loginButton);

        createAccountButton = new JButton("Create Account");
        panel.add(createAccountButton);

        add(panel);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addCreateAccountButtonListener(ActionListener listener) {
        createAccountButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}