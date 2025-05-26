import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class QuizScreen extends JFrame {
    private JLabel questionLabel;
    private JButton revealButton, correctButton, incorrectButton;
    private JTextArea answerArea;
    private List<Flashcard> flashcards;
    private Iterator<Flashcard> iterator;
    private Flashcard currentCard;
    private int score = 0;

    public QuizScreen(User user) {
        setTitle("Quiz");
        setSize(400, 300);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Question");
        answerArea = new JTextArea();
        answerArea.setEditable(false);
        answerArea.setVisible(false);

        revealButton = new JButton("Reveal Answer");
        correctButton = new JButton("Correct");
        incorrectButton = new JButton("Incorrect");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(revealButton);
        buttonPanel.add(correctButton);
        buttonPanel.add(incorrectButton);

        add(questionLabel, BorderLayout.NORTH);
        add(answerArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        correctButton.setEnabled(false);
        incorrectButton.setEnabled(false);

        loadFlashcards(user);
        showNextCard();

        revealButton.addActionListener(e -> {
            answerArea.setVisible(true);
            correctButton.setEnabled(true);
            incorrectButton.setEnabled(true);
        });

        correctButton.addActionListener(e -> {
            score++;
            showNextCard();
        });

        incorrectButton.addActionListener(e -> showNextCard());

        setVisible(true);
    }

    private void loadFlashcards(User user) {
        flashcards = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flashcards WHERE user_id=?");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                flashcards.add(new Flashcard(
                    rs.getInt("id"),
                    rs.getString("question"),
                    rs.getString("answer")
                ));
            }
            Collections.shuffle(flashcards);
            iterator = flashcards.iterator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showNextCard() {
        if (iterator.hasNext()) {
            currentCard = iterator.next();
            questionLabel.setText(currentCard.getQuestion());
            answerArea.setText(currentCard.getAnswer());
            answerArea.setVisible(false);
            correctButton.setEnabled(false);
            incorrectButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Quiz finished! Score: " + score);
            dispose();
            new FlashcardManager(new User(1, "Test")); 
        }
    }
}
