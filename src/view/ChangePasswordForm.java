package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import dao.StudentDAO;
import util.UIConstants;
import util.UserSession;

public class ChangePasswordForm extends JPanel {

    private final JPasswordField txtOldPassword;
    private final JPasswordField txtNewPassword;
    private final JPasswordField txtConfirmPassword;

    public ChangePasswordForm() {
        setBackground(UIConstants.BACKGROUND);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        card.setPreferredSize(new Dimension(450, 420));

        JLabel title = new JLabel("Change Account Password");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UIConstants.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Update security credentials for your portal access");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel grid = new JPanel(new GridLayout(3, 2, 15, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel lblOld = new JLabel("Current Password");
        lblOld.setFont(UIConstants.BOLD_FONT);
        txtOldPassword = new JPasswordField();
        txtOldPassword.setFont(UIConstants.NORMAL_FONT);

        JLabel lblNew = new JLabel("New Password");
        lblNew.setFont(UIConstants.BOLD_FONT);
        txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(UIConstants.NORMAL_FONT);

        JLabel lblConfirm = new JLabel("Confirm New Password");
        lblConfirm.setFont(UIConstants.BOLD_FONT);
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(UIConstants.NORMAL_FONT);

        grid.add(lblOld);
        grid.add(txtOldPassword);
        grid.add(lblNew);
        grid.add(txtNewPassword);
        grid.add(lblConfirm);
        grid.add(txtConfirmPassword);

        JButton btnChange = new JButton("Change Password");
        btnChange.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnChange.setBackground(UIConstants.BUTTON);
        btnChange.setForeground(Color.WHITE);
        btnChange.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChange.setFocusPainted(false);
        btnChange.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnChange.addActionListener(e -> changePassword());

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(grid);
        card.add(btnChange);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(card, gbc);
    }

    private void changePassword() {
        String oldPassword = String.valueOf(txtOldPassword.getPassword());
        String newPassword = String.valueOf(txtNewPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());

        if (oldPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all password fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "New password and confirm password do not match.",
                    "Passwords Mismatch",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 6 characters long.",
                    "Security Requirement",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        StudentDAO dao = new StudentDAO();
        boolean result = dao.changePassword(
                UserSession.studentEmail,
                oldPassword,
                newPassword
        );

        if (result) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your portal password has been successfully updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Clear inputs
            txtOldPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update password. Please check if your current password is correct.",
                    "Change Password Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}