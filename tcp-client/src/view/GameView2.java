package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameView2 extends JFrame {
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel buttonPanel; // Panel chứa nút Submit và Skip
    private ArrayList<JLabel> centerLabels;
    private ArrayList<JButton> letterButtons;
    private String[] words = {"hello", "abc", "iloveyou"};
    private int i = 0;
    private Map<JLabel, JButton> centerToBottomMap = new HashMap<>();
    private JLabel timerLabel; 
    private Timer timer; 
    private int timeLeft = 10;

    public GameView2() {
        setTitle("Game ô chữ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // Tùy chỉnh kích thước cửa sổ
        setLayout(new BorderLayout());

        // Tạo panel hiển thị thời gian
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel = new JLabel("Thời gian còn lại: " + timeLeft + " giây", JLabel.LEFT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerPanel.add(timerLabel);
        add(timerPanel, BorderLayout.NORTH); // Đặt panel thời gian ở vị trí NORTH

        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        add(centerPanel, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 5, 10, 10)); // Sử dụng GridLayout
        add(bottomPanel, BorderLayout.SOUTH);

        buttonPanel = new JPanel(); // Panel mới để chứa nút Submit và Skip
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Căn giữa với khoảng cách tùy chỉnh
        add(buttonPanel, BorderLayout.EAST); // Đặt panel ở phía dưới, dưới bottomPanel

        // Thêm nút Submit
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 50)); // Tùy chỉnh kích thước nút
        submitButton.addActionListener(e -> checkWord());
        buttonPanel.add(submitButton); // Thêm nút vào panel

        // Thêm nút Skip
        JButton skipButton = new JButton("Skip");
        skipButton.setPreferredSize(new Dimension(100, 50));
        skipButton.addActionListener(e -> loadNextWord());
        buttonPanel.add(skipButton); 

        centerLabels = new ArrayList<>();
        letterButtons = new ArrayList<>();
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Thời gian còn lại: " + timeLeft + " giây");
                } else {
                    timer.stop();
                    JOptionPane.showMessageDialog(GameView2.this, "Hết thời gian!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    // Xử lý khi hết thời gian ở đây (ví dụ: kết thúc trò chơi)
                }
            }
        });
        timer.start(); // Bắt đầu đếm ngược

        loadNextWord();

        setVisible(true);
    }

    public void resetTimer() {
        timeLeft = 150; // Đặt lại thời gian
        timerLabel.setText("Thời gian còn lại: " + timeLeft + " giây");
        timer.start(); // Bắt đầu lại timer
    }
    
    private String[] getScrambledLetters() {
        if (i >= words.length) i = 0;
        String word = words[i++];
        ArrayList<String> letters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            letters.add(String.valueOf(c));
        }
        Collections.shuffle(letters);
        return letters.toArray(new String[0]);
    }

    // Hàm để tải từ tiếp theo
    private void loadNextWord() {
        String[] scrambledLetters = getScrambledLetters();

        // Cập nhật lại các ô hiển thị ở giữa màn hình
        centerPanel.removeAll();
        centerLabels.clear();

        for (int j = 0; j < scrambledLetters.length; j++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setPreferredSize(new Dimension(50, 50)); 
            label.setFont(new Font("Arial", Font.PLAIN, 32));

            // Thêm CenterLabelMouseListener cho mỗi ô ở centerPanel
            label.addMouseListener(new CenterLabelMouseListener(label));

            centerPanel.add(label);
            centerLabels.add(label);
        }
        centerPanel.revalidate();
        centerPanel.repaint();

        // Cập nhật lại các nút chữ cái ở dưới màn hình
        bottomPanel.removeAll();
        bottomPanel.setLayout(new GridLayout(1, scrambledLetters.length, 10, 10)); 
        letterButtons.clear();

        for (String letter : scrambledLetters) {
            JButton button = new JButton(letter);
            button.setPreferredSize(new Dimension(50, 50)); // Điều chỉnh kích thước các nút ở bottomPanel
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.addActionListener(new LetterButtonListener());
            bottomPanel.add(button);
            letterButtons.add(button);
        }
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    // Kiểm tra từ khi ấn Submit
    private void checkWord() {
        StringBuilder sb = new StringBuilder();
        for (JLabel label : centerLabels) {
            sb.append(label.getText());
        }

        String guessedWord = sb.toString();
        String correctWord = words[i - 1]; // Lấy từ đúng để so sánh

        if (guessedWord.equals(correctWord)) {
            JOptionPane.showMessageDialog(this, "Correct!");
            loadNextWord();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect!");
        }
    }
    

    // Lớp lắng nghe sự kiện nhấn vào các ô ở centerPanel
    private class CenterLabelMouseListener extends MouseAdapter {
        private final JLabel label;

        public CenterLabelMouseListener(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel clickedLabel = (JLabel) e.getSource();
            String letter = clickedLabel.getText();

            if (!letter.isEmpty()) {
                clickedLabel.setText(""); // Xóa chữ từ centerPanel

                // Khôi phục chữ cái về nút trong bottomPanel
                JButton correspondingButton = centerToBottomMap.get(clickedLabel);
                if (correspondingButton != null) {
                    correspondingButton.setText(letter);
                }
            }
        }
    }

    // Lớp lắng nghe sự kiện nhấn vào các nút ở bottomPanel
    private class LetterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String letter = button.getText();

            for (JLabel label : centerLabels) {
                if (label.getText().isEmpty()) {
                    label.setText(letter);

                    // Liên kết giữa label ở centerPanel và button ở bottomPanel
                    centerToBottomMap.put(label, button);

                    button.setText("");
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameView2();
    }
}
