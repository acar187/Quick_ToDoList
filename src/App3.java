import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Map;
import java.awt.font.TextAttribute;

public class App3 {
    private JTextPane textPane;
    private boolean isBold = false, isItalic = false, isUnderlined = false, isStrikethrough = false, isParagraph = false;
    private JButton bnew, bbold, bitalic, bunderline, bstrikethrough, bparagraph;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App3().createAndShowGUI());
    }

    private void createAndShowGUI() {
        setLookAndFeel();
        
        JFrame frame = new JFrame("ToDo List");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTasks();
                frame.dispose();
            }
        });
    
    textPane = new JTextPane();
    textPane.setFont(new Font("Arial", Font.PLAIN, 18));        
    textPane.setEditable(true);
    JScrollPane scrollPane = new JScrollPane(textPane);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setViewportBorder(BorderFactory.createTitledBorder("ToDo List"));
    
    JPanel buttonPanelMain = createButtonPanel();
    // Add the button panel to the frame
    
    frame.add(BorderLayout.NORTH, buttonPanelMain);
    // Add the scroll pane to the frame
    frame.add(BorderLayout.CENTER,scrollPane);
    // Set the frame to be visible
    frame.setVisible(true);
    // Load tasks from file
    loadTasks();
    
    }
    private JPanel createButtonPanel() {
        JPanel buttonPanelMain = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanelLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bnew = createButton("New", Font.BOLD, null, "Create a new task", e -> resetFormatting());
        buttonPanelLeft.add(bnew);

        bbold = createButton("B", Font.BOLD, null, "Toggle bold text", e -> toggleTextStyle(StyleConstants.Bold, bbold));
        buttonPanel.add(bbold);

        bitalic = createButton("I", Font.ITALIC, null, "Toggle italic text", e -> toggleTextStyle(StyleConstants.Italic, bitalic));
        buttonPanel.add(bitalic);

        bunderline = createButton("U", Font.PLAIN, null, "Toggle underline text", e -> toggleTextStyle(StyleConstants.Underline, bunderline));
        underlineButtonText(bunderline);
        buttonPanel.add(bunderline);

        bstrikethrough = createButton("S", Font.PLAIN, null, "Toggle strikethrough text", e -> toggleTextStyle(StyleConstants.StrikeThrough, bstrikethrough));
        strikethroughButtonText(bstrikethrough);
        buttonPanel.add(bstrikethrough);

        bparagraph = createButton("Title", Font.PLAIN, null, "Toggle paragraph alignment", e -> toggleParagraphStyle());
        buttonPanel.add(bparagraph);
        buttonPanelMain.add(buttonPanelLeft);
        buttonPanelMain.add(buttonPanel);
        buttonPanelMain.setBackground(Color.LIGHT_GRAY);
        return buttonPanelMain;
        }

    private void toggleTextStyle(Object style, JButton button) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        boolean isActive = false;
    
        if (style == StyleConstants.Bold) {
            isActive = isBold = !isBold;
            StyleConstants.setBold(attr, isBold);
        } else if (style == StyleConstants.Italic) {
            isActive = isItalic = !isItalic;
            StyleConstants.setItalic(attr, isItalic);
        } else if (style == StyleConstants.Underline) {
            isActive = isUnderlined = !isUnderlined;
            StyleConstants.setUnderline(attr, isUnderlined);
        } else if (style == StyleConstants.StrikeThrough) {
            isActive = isStrikethrough = !isStrikethrough;
            StyleConstants.setStrikeThrough(attr, isStrikethrough);
        }
    
        // Wende die Textattribute an
        textPane.setCharacterAttributes(attr, false);
        button.setBackground(isActive ? Color.ORANGE : null); // Setze die Hintergrundfarbe
    
        // Setze den Fokus zurück auf das JTextPane
        textPane.requestFocusInWindow();
    }

    private void resetFormatting() {
        textPane.setText("");
        textPane.requestFocusInWindow();
        bbold.setBackground(null);
        bitalic.setBackground(null);
        bunderline.setBackground(null);
        bstrikethrough.setBackground(null);
        bparagraph.setBackground(null);
        isBold = isItalic = isUnderlined = isStrikethrough = isParagraph = false;

        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyledDocument doc = textPane.getStyledDocument();

        StyleConstants.setBold(attr, false);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setUnderline(attr, false);
        StyleConstants.setStrikeThrough(attr, false);
        textPane.setCharacterAttributes(attr, false);
        StyleConstants.setFontSize(attr, 18); // Standard-Schriftgröße
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attr, false);
    }

    private JButton createButton(String text, int fontStyle, Color bgColor, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", fontStyle, 18));
        button.setToolTipText(tooltip);
        if (bgColor != null) button.setBackground(bgColor);
        button.addActionListener(action);
        return button;
    }

    private void underlineButtonText(JButton button) {
        Font font = button.getFont();
        Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        button.setFont(font.deriveFont(attributes));
    }

    
    
    private void strikethroughButtonText(JButton button) {
        Font font = button.getFont();
        Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        button.setFont(font.deriveFont(attributes));
    }

    private void toggleParagraphStyle() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyledDocument doc = textPane.getStyledDocument();

        if (!isParagraph) {
            StyleConstants.setFontSize(attr, 26);
            StyleConstants.setBold(attr, true);
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
            bparagraph.setBackground(Color.ORANGE);
        } else {
            StyleConstants.setFontSize(attr, 18);
            StyleConstants.setBold(attr, false);
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
            bparagraph.setBackground(null);
        }

        doc.setParagraphAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attr, false);
        isParagraph = !isParagraph;
        textPane.requestFocusInWindow();
    }

    public void saveTasks() {
        String text = textPane.getText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("todo.txt"))) {
            writer.write(text);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("todo.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textPane.setText(textPane.getText() + line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
