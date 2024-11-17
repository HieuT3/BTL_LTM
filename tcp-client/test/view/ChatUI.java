package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatUI extends JFrame {

    private JPanel chatPanel;
    private JTextField inputField;
    private JButton sendButton;

    public ChatUI() {
        setTitle("Chat with hieucb98");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel with the chat title and Leave button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel chatTitle = new JLabel("Chat with hieucb98", JLabel.LEFT);
        chatTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        JButton leaveButton = new JButton("Leave");
        leaveButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Set background color and padding
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        headerPanel.add(chatTitle, BorderLayout.WEST);
        headerPanel.add(leaveButton, BorderLayout.EAST);

        // Chat panel to hold messages
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Footer with input field and send button
        JPanel footerPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        footerPanel.add(inputField, BorderLayout.CENTER);
        footerPanel.add(sendButton, BorderLayout.EAST);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Sample chat messages
        addMessage("Hi my friend!!!", false);
        addMessage("Do you mind sending me the brief for the new campaign? bla bla bla bla...", false);
        addMessage("Hey, what's up!!!", true);
        addMessage("Of course. bla bla bla bla...", true);

        // Send button action
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Press Enter to send message
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    // Method to add a message bubble to the chat panel
    private void addMessage(String message, boolean isUser) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel messageLabel = new JLabel("<html><p style=\"width: 200px;\">" + message + "</p></html>");
        messageLabel.setOpaque(true);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (isUser) {
            messageLabel.setBackground(new Color(220, 248, 198)); // User message color
            messagePanel.add(messageLabel, BorderLayout.EAST);
        } else {
            messageLabel.setBackground(new Color(235, 235, 255)); // Other person message color
            messagePanel.add(messageLabel, BorderLayout.WEST);
        }

        // Add rounded border to message bubble
        messageLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15), new EmptyBorder(10, 10, 10, 10)));

        chatPanel.add(messagePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    // Method to handle sending a message
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            addMessage(message, true);
            inputField.setText("");
        }
    }

    // Custom rounded border class
    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius + 1;
            return insets;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatUI chatUI = new ChatUI();
            chatUI.setVisible(true);
        });
    }
}


