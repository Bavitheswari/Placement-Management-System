package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import dao.StudentDAO;
import model.Student;
import util.UIConstants;
import util.UserSession;
import util.components.AnimatedButton;

public class UpdateProfileForm extends JPanel {

    private final GlowingTextField txtDepartment;
    private final GlowingTextField txtCgpa;
    private final GlowingTextField txtPhone;

    public UpdateProfileForm() {
        setBackground(new Color(235, 240, 245)); // Soft backdrop
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Glassmorphism Card
        JPanel glassCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Translucent white for glass effect
                g2.setColor(new Color(255, 255, 255, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // Subtle white border for glass edge
                g2.setStroke(new BasicStroke(1.5f));
                g2.setColor(new Color(255, 255, 255, 230));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
                
                g2.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setLayout(new BoxLayout(glassCard, BoxLayout.Y_AXIS));
        glassCard.setBorder(new EmptyBorder(35, 45, 35, 45));
        glassCard.setPreferredSize(new Dimension(480, 440));

        JLabel title = new JLabel("Update Profile Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Keep your academic and contact details up to date");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel grid = new JPanel(new GridLayout(3, 1, 0, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(25, 0, 30, 0));

        txtDepartment = new GlowingTextField("Academic Department", "e.g. Computer Science");
        txtCgpa = new GlowingTextField("Cumulative CGPA", "e.g. 8.5");
        txtPhone = new GlowingTextField("Phone Number", "e.g. +91 9876543210");

        grid.add(txtDepartment);
        grid.add(txtCgpa);
        grid.add(txtPhone);

        AnimatedButton btnUpdate = new AnimatedButton("Save Changes", new Color(14, 165, 233));
        btnUpdate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnUpdate.addActionListener(e -> updateProfile());

        glassCard.add(title);
        glassCard.add(Box.createVerticalStrut(8));
        glassCard.add(subtitle);
        glassCard.add(grid);
        glassCard.add(btnUpdate);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        add(glassCard, gbc);

        loadStudentData();
    }

    private void loadStudentData() {
        try {
            StudentDAO dao = new StudentDAO();
            Student student = dao.getStudentByEmail(UserSession.studentEmail);
            if (student != null) {
                txtDepartment.setText(student.getDepartment());
                txtCgpa.setText(String.valueOf(student.getCgpa()));
                txtPhone.setText(student.getPhone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        if (txtDepartment.getText().trim().isEmpty() ||
            txtCgpa.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double cgpa;
        try {
            cgpa = Double.parseDouble(txtCgpa.getText().trim());
            if (cgpa < 0.0 || cgpa > 10.0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CGPA must be a valid number between 0.0 and 10.0", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            StudentDAO dao = new StudentDAO();
            Student student = dao.getStudentByEmail(UserSession.studentEmail);
            
            if (student != null) {
                student.setDepartment(txtDepartment.getText().trim());
                student.setCgpa(cgpa);
                student.setPhone(txtPhone.getText().trim());
                
                if (dao.updateStudentProfile(student)) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update profile.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Custom Animated Glowing TextField ---
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
            lbl.setForeground(new Color(71, 85, 105));
            lbl.setBorder(new EmptyBorder(0, 4, 4, 0));

            field = new JTextField() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (glowAlpha > 0) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(56, 189, 248, (int)(glowAlpha * 100)));
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                        g2.dispose();
                    }
                }
            };
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
            ));
            field.putClientProperty("JTextField.placeholderText", placeholder);
            field.setOpaque(false);
            field.setBackground(new Color(255, 255, 255, 200)); // Slightly translucent background

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
    }
}
