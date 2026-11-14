package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import dao.StudentDAO;
import util.UIConstants;
import util.UserSession;

public class StudentLoginForm extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public StudentLoginForm() {
        setTitle("Student Login - Placement Management System");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel loginCard = new JPanel();
        loginCard.setPreferredSize(new Dimension(450, 520));
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

        JLabel portalLabel = new JLabel("STUDENT PORTAL");
        portalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        portalLabel.setForeground(UIConstants.BUTTON);
        portalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to check placement drives and upload resumes");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(UIConstants.BOLD_FONT);
        lblEmail.setForeground(UIConstants.TEXT_DARK);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtEmail.setFont(UIConstants.NORMAL_FONT);
        txtEmail.putClientProperty("JTextField.placeholderText", "Enter your registered email");

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

        // Register action link
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        regPanel.setOpaque(false);
        JLabel lblNoAccount = new JLabel("New here?");
        lblNoAccount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNoAccount.setForeground(UIConstants.TEXT_MUTED);
        
        JButton btnRegister = new JButton("Create an account");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegister.setForeground(UIConstants.BUTTON);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setBorder(null);
        
        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegister.setForeground(UIConstants.BUTTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnRegister.setForeground(UIConstants.BUTTON);
            }
        });
        
        btnRegister.addActionListener(e -> {
            dispose();
            new StudentRegistrationForm();
        });

        regPanel.add(lblNoAccount);
        regPanel.add(btnRegister);

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
        JPanel wrapEmail = new JPanel(new BorderLayout());
        wrapEmail.setOpaque(false);
        wrapEmail.add(lblEmail, BorderLayout.NORTH);
        wrapEmail.add(Box.createVerticalStrut(6), BorderLayout.CENTER);
        wrapEmail.add(txtEmail, BorderLayout.SOUTH);
        wrapEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(wrapEmail);

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
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(regPanel);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(loginCard, gbc);

        add(mainPanel);

        btnLogin.addActionListener(e -> loginStudent());
        
        // Listen to Enter key on password field
        txtPassword.addActionListener(e -> loginStudent());

        setVisible(true);
    }

    private void loginStudent() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both email and password.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        StudentDAO dao = new StudentDAO();
        boolean result = dao.loginStudent(email, password);

        if (result) {
            UserSession.studentEmail = email;
            String studentName = dao.getStudentName(email, password);
            if (studentName == null || studentName.trim().isEmpty()) {
                studentName = "Student";
            }
            try {
                StudentDashboard dashboard = new StudentDashboard(studentName, email);
                dashboard.setVisible(true);
                dispose();
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to load dashboard: " + t.getMessage(),
                        "System Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid student email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

