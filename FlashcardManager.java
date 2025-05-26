import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class FlashcardManager extends JFrame {
    private JTextArea questionArea, answerArea;
    private JButton saveButton, quizButton, deleteButton;
    private JList<String> flashcardList;
    private DefaultListModel<String> listModel;
    private java.util.List<Flashcard> flashcards;
    private User user;

    public FlashcardManager(User user) {
        this.user = user;
        setTitle("Flashcard Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Area
        JPanel inputPanel = new JPanel(new GridLayout(4, 1));
        questionArea = new JTextArea("Question");
        answerArea = new JTextArea("Answer");
        saveButton = new JButton("Save Flashcard");
        deleteButton = new JButton("Delete Selected");

        inputPanel.add(new JLabel("Question:"));
        inputPanel.add(questionArea);
        inputPanel.add(new JLabel("Answer:"));
        inputPanel.add(answerArea);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Flashcard List
        listModel = new DefaultListModel<>();
        flashcardList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(flashcardList);
        loadFlashcards();

        flashcardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flashcardList.addListSelectionListener(e -> showSelectedFlashcard());

        // Quiz Button
        quizButton = new JButton("Start Quiz");
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(quizButton, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Button Listeners
        saveButton.addActionListener(e -> saveFlashcard());
        deleteButton.addActionListener(e -> deleteFlashcard());
        quizButton.addActionListener(e -> {
            dispose();
            new QuizScreen(user);
        });

        setVisible(true);
    }

    private void loadFlashcards() {
        flashcards = new ArrayList<>();
        listModel.clear();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flashcards WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                flashcards.add(new Flashcard(id, question, answer));
                listModel.addElement(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showSelectedFlashcard() {
        int index = flashcardList.getSelectedIndex();
        if (index >= 0) {
            Flashcard fc = flashcards.get(index);
            questionArea.setText(fc.getQuestion());
            answerArea.setText(fc.getAnswer());
        }
    }

    private void saveFlashcard() {
        String question = questionArea.getText();
        String answer = answerArea.getText();

        if (question.isEmpty() || answer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO flashcards (user_id, question, answer) VALUES (?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, question);
            stmt.setString(3, answer);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Flashcard Saved!");
            loadFlashcards();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFlashcard() {
        int selectedIndex = flashcardList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Flashcard fc = flashcards.get(selectedIndex);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this flashcard?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM flashcards WHERE id = ?");
                    stmt.setInt(1, fc.getId());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Flashcard Deleted.");
                    loadFlashcards();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flashcard to delete.");
        }
    }
}
