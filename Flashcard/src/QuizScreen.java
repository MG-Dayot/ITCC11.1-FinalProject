import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizScreen extends JFrame {
    private ArrayList<FlashcardPanel> flashcards = new ArrayList<>();
    private JPanel flashcardContainer;
    private JButton createFlashcardButton;

    public QuizScreen() {
        setTitle("Flashcard Quiz");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for displaying flashcards
        flashcardContainer = new JPanel();
        flashcardContainer.setLayout(new GridLayout(0, 1, 10, 10));  // Stack flashcards vertically
        add(new JScrollPane(flashcardContainer), BorderLayout.CENTER);

        // Button to create a new flashcard
        createFlashcardButton = new JButton("Create Flashcard");
        createFlashcardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFlashcard();  // Open dialog to create a new flashcard
            }
        });
        add(createFlashcardButton, BorderLayout.SOUTH);

        // Fetch flashcards from the database
        loadFlashcards();
    }

    // Method to load flashcards from the database
    private void loadFlashcards() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT question, answer FROM flashcards");

            // Clear existing flashcards
            flashcardContainer.removeAll();

            // Fetch flashcards from the result set and display them
            while (rs.next()) {
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                FlashcardPanel flashcard = new FlashcardPanel(question, answer);
                flashcards.add(flashcard);
                flashcardContainer.add(flashcard);
            }

            // Refresh the panel
            flashcardContainer.revalidate();
            flashcardContainer.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading flashcards: " + e.getMessage());
        } finally {
            DatabaseConnector.closeConnection(conn, stmt, rs);
        }
    }

    // Method to create a new flashcard and add it to the database
    private void createFlashcard() {
        // Show input dialog to get question and answer
        String question = JOptionPane.showInputDialog(this, "Enter the question:");
        String answer = JOptionPane.showInputDialog(this, "Enter the answer:");

        if (question != null && answer != null && !question.trim().isEmpty() && !answer.trim().isEmpty()) {
            // Add the new flashcard to the database
            addFlashcardToDatabase(question, answer);

            // Add the flashcard to the panel
            FlashcardPanel flashcard = new FlashcardPanel(question, answer);
            flashcards.add(flashcard);
            flashcardContainer.add(flashcard);  // Add to the flashcard container
            flashcardContainer.revalidate();  // Refresh the panel
            flashcardContainer.repaint();  // Redraw the panel
        } else {
            JOptionPane.showMessageDialog(this, "Both fields must be filled!");
        }
    }

    // Method to add the new flashcard to the database
    private void addFlashcardToDatabase(String question, String answer) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnector.getConnection();
            String sql = "INSERT INTO flashcards (question, answer) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, question);
            stmt.setString(2, answer);

            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding flashcard: " + e.getMessage());
        } finally {
            DatabaseConnector.closeConnection(conn, stmt, null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizScreen().setVisible(true);  // Show the quiz screen with the flashcards
        });
    }
}
