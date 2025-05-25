import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardPanel extends JPanel {
    private boolean showingAnswer = false;  // To track if the answer is showing
    private String question;
    private String answer;
    private JLabel label;

    // Constructor to initialize the flashcard with a question and answer
    public FlashcardPanel(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.label = new JLabel(question, SwingConstants.CENTER);

        // Set up the panel to look like a card
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 200));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));  // Border color (SteelBlue)
        setMaximumSize(new Dimension(300, 200));  // Prevent panel from resizing too much
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // Set hand cursor on hover
        setOpaque(false); // Make the background transparent for shadow effect

        // Add a rounded label for question text with custom font
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.BLACK);  // Set text color for question
        label.setBackground(Color.WHITE);  // Set label background color
        label.setOpaque(true); // Make label's background opaque
        label.setPreferredSize(new Dimension(280, 180));  // Slight padding inside the card

        // Add the label to the center of the panel
        add(label, BorderLayout.CENTER);

        // Add a MouseListener to flip the card when clicked
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flipCard();  // Flip the card on click
            }
        });
    }

    // Method to flip the card
    private void flipCard() {
        showingAnswer = !showingAnswer;  // Toggle between showing question and answer
        if (showingAnswer) {
            label.setText(answer);  // Show the answer
            label.setBackground(new Color(255, 245, 204));  // Light yellow background for answer
            label.setForeground(new Color(255, 102, 102));  // Red text for the answer
        } else {
            label.setText(question);  // Show the question
            label.setBackground(Color.WHITE);  // White background for question
            label.setForeground(Color.BLACK);  // Black text for the question
        }
    }

    // Optionally, you can override the paint method to add more customization to the card's look
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the card's shadow (optional for extra styling)
        g.setColor(new Color(169, 169, 169));  // Light gray shadow color
        g.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);  // Rounded corners shadow effect
    }
}