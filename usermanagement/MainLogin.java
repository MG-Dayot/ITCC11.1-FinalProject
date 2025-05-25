
public class MainLogin {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {

            Login login = new Login();
            UserManager userManager = new UserManager();
            LoginController loginController = new LoginController(login, userManager);
            login.setVisible(true);
        });
    }
}