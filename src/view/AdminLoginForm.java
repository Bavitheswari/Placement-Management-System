package view;

import dao.AdminDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import util.UIConstants;

public class AdminLoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public AdminLoginForm() {
        setTitle("Admin Login - Placement Management System");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel loginCard = new JPanel();
        loginCard.setPreferredSize(new Dimension(450, 480));
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(UIConstants.CARD_BG);
        
        // Compound border for modern outline and internal padding
        loginCard.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(35, 45, 35, 45)));

        JLabel appTitle = new JLabel("Placement Portal");
        appTitle.setFont(UIConstants.TITLE_FONT);
        appTitle.setForeground(UIConstants.TEXT_DARK);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel portalLabel = new JLabel("ADMIN CONTROL PANEL");
        portalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        portalLabel.setForeground(UIConstants.BUTTON);
        portalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Authenticate to access administration dashboard");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(UIConstants.BOLD_FONT);
        lblUsername.setForeground(UIConstants.TEXT_DARK);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setFont(UIConstants.NORMAL_FONT);
        txtUsername.putClientProperty("JTextField.placeholderText", "Enter admin username");

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(UIConstants.BOLD_FONT);
        lblPassword.setForeground(UIConstants.TEXT_DARK);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setFont(UIConstants.NORMAL_FONT);
        txtPassword.putClientProperty("JTextField.placeholderText", "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022");

        JButton btnLogin = new JButton("LOG IN");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setBackground(UIConstants.BUTTON);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(UIConstants.BUTTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(UIConstants.BUTTON);
            }
        });

        // Back to Home Link
        JButton btnBack = new JButton("<- Back to Portal Selection");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setForeground(UIConstants.TEXT_MUTED);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setForeground(UIConstants.BUTTON);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setForeground(UIConstants.TEXT_MUTED);
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new HomePage();
        });

        // Add components to the login card box layout
        loginCard.add(appTitle);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(portalLabel);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(subtitle);
        loginCard.add(Box.createVerticalStrut(30));
        
        // Wrap input fields in components that set alignments
        JPanel wrapUser = new JPanel(new BorderLayout());
        wrapUser.setOpaque(false);
        wrapUser.add(lblUsername, BorderLayout.NORTH);
        wrapUser.add(Box.createVerticalStrut(6), BorderLayout.CENTER);
        wrapUser.add(txtUsername, BorderLayout.SOUTH);
        wrapUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(wrapUser);

        loginCard.add(Box.createVerticalStrut(15));

        JPanel wrapPass = new JPanel(new BorderLayout());
        wrapPass.setOpaque(false);
        wrapPass.add(lblPassword, BorderLayout.NORTH);
        wrapPass.add(Box.createVerticalStrut(6), BorderLayout.CENTER);
        wrapPass.add(txtPassword, BorderLayout.SOUTH);
        wrapPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(wrapPass);

        loginCard.add(Box.createVerticalStrut(30));
        loginCard.add(btnLogin);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(loginCard, gbc);

        add(mainPanel);

        btnLogin.addActionListener(e -> loginAdmin());
        
        // Listen to Enter key on password field
        txtPassword.addActionListener(e -> loginAdmin());

        setVisible(true);
    }

    private void loginAdmin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        AdminDAO dao = new AdminDAO();
        boolean result = dao.loginAdmin(username, password);

        if (result) {
            dispose();
            new DashboardFrame();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid admin credentials. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}