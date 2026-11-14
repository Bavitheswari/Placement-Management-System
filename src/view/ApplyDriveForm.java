package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import dao.ApplicationDAO;
import dao.DriveDAO;
import dao.StudentDAO;
import model.Application;
import util.UserSession;
import util.components.AnimatedButton;
import util.components.AnimatedCardPanel;

public class ApplyDriveForm extends JPanel {

    private final GlowingTextField txtDriveId;

    public ApplyDriveForm() {
        setBackground(new Color(248, 250, 252));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        AnimatedCardPanel card = new AnimatedCardPanel(new Color(14, 165, 233));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 45, 35, 45));
        card.setPreferredSize(new Dimension(450, 360));

        JLabel iconLbl = new JLabel("");
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Direct Application");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Submit your profile directly via Drive ID");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(224, 242, 254));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel grid = new JPanel(new GridLayout(2, 1, 0, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 0, 30, 0));

        // Read-only email field for reference
        GlowingTextField txtEmail = new GlowingTextField("Student Email", "");
        txtEmail.setText(UserSession.studentEmail);
        txtEmail.setEnabled(false);

        txtDriveId = new GlowingTextField("Target Drive ID", "e.g. 5");

        grid.add(txtEmail);
        grid.add(txtDriveId);

        AnimatedButton btnApply = new AnimatedButton("Submit Application", new Color(248, 250, 252));
        btnApply.setForeground(new Color(14, 165, 233)); // Blue text on white button
        btnApply.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnApply.addActionListener(e -> applyDrive());

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(grid);
        card.add(btnApply);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        add(card, gbc);
    }

    private void applyDrive() {
        String driveIdStr = txtDriveId.getText().trim();
        if (driveIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Drive ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int driveId;
        try {
            driveId = Integer.parseInt(driveIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Drive ID must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            StudentDAO studentDAO = new StudentDAO();
            int studentId = studentDAO.getStudentIdByEmail(UserSession.studentEmail);

            // Check if drive exists & meets cgpa
            DriveDAO driveDAO = new DriveDAO();
            double minCgpa = driveDAO.getMinCgpaForDrive(driveId);

            if (minCgpa == -1) {
                JOptionPane.showMessageDialog(this, "No hiring drive found with ID: " + driveId, "Invalid Drive", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double studentCgpa = studentDAO.getStudentCgpa(studentId);
            if (studentCgpa < minCgpa) {
                JOptionPane.showMessageDialog(this, "You are not eligible for this drive. Minimum CGPA required is " + minCgpa + ", your CGPA is " + studentCgpa, "Eligibility Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check resume
            String resumePath = studentDAO.getResumePathByStudentId(studentId);
            if(resumePath == null || resumePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must upload your resume before applying.", "Resume Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if already applied
            ApplicationDAO appDAO = new ApplicationDAO();
            if (appDAO.alreadyApplied(studentId, driveId)) {
                JOptionPane.showMessageDialog(this, "You have already applied for this drive.", "Duplicate Application", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Create Application
            Application app = new Application();
            app.setStudentId(studentId);
            app.setDriveId(driveId);
            app.setStatus("Pending");

            if (appDAO.applyForDrive(app)) {
                JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtDriveId.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application. Please try again.", "System Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Custom Animated Glowing TextField (adapted for blue card background) ---
    class GlowingTextField extends JPanel {
        private JTextField field;
        private float glowAlpha = 0f;
        private Timer focusTimer;
        private boolean isFocused = false;

        public GlowingTextField(String labelText, String placeholder) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(0, 0, 5, 0));

            JLabel lbl = new JLabel(labelText);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(new Color(224, 242, 254)); // Light blue text
            lbl.setBorder(new EmptyBorder(0, 4, 4, 0));

            field = new JTextField() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (glowAlpha > 0) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(255, 255, 255, (int)(glowAlpha * 180)));
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                        g2.dispose();
                    }
                }
            };
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true),
                new EmptyBorder(8, 12, 8, 12)
            ));
            field.putClientProperty("JTextField.placeholderText", placeholder);
            field.setOpaque(false);
            field.setBackground(new Color(255, 255, 255, 30)); // Translucent white

            focusTimer = new Timer(15, e -> {
                if (isFocused && glowAlpha < 1f) { glowAlpha = Math.min(1f, glowAlpha + 0.15f); field.repaint(); }
                else if (!isFocused && glowAlpha > 0f) { glowAlpha = Math.max(0f, glowAlpha - 0.15f); field.repaint(); }
                else focusTimer.stop();
            });

            field.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { isFocused = true; focusTimer.start(); }
                @Override public void focusLost(FocusEvent e) { isFocused = false; focusTimer.start(); }
            });

            add(lbl, BorderLayout.NORTH);
            add(field, BorderLayout.CENTER);
        }

        public String getText() { return field.getText(); }
        public void setText(String text) { field.setText(text); }
        public void setEnabled(boolean enabled) { field.setEditable(enabled); field.setFocusable(enabled); }
    }
}