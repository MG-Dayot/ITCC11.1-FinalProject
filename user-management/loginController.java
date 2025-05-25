import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginController {
    private Login view;
    private UserManager userManager;

    public loginController(Login view, UserManager userManager) {
        this.view = view;
        this.userManager = userManager;

        this.view.addLoginButtonListener(new LoginButtonListener());
        this.view.addCreateAccountButtonListener(new CreateAccountButtonListener());
    }

    class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if (userManager.validateUser(username, password)) {
                view.showMessage("Login successful! Welcome, " + username + "!");
                // flashcard screen here
            } else {
                view.showMessage("Invalid username or password.");
            }
        }
    }

    class CreateAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showMessage("Username and password cannot be empty.");
                return;
            }

            if (userManager.addUser(username, password)) {
                view.showMessage("Account created successfully! You can now log in.");
            } else {
                view.showMessage("Failed to create account. Please try again.");
            }
        }
    }
}