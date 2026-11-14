package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import util.components.AnimatedButton;

public class AdminDashboard extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    public AdminDashboard() {
        setTitle("Placement Management System");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === DARK HEADER ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 23, 42));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Placement Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Admin Control Panel");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(148, 163, 184));

        JPanel titleBox = new JPanel(new BorderLayout());
        titleBox.setOpaque(false);
        titleBox.add(title, BorderLayout.NORTH);
        titleBox.add(subtitle, BorderLayout.SOUTH);
        headerPanel.add(titleBox, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // === CENTER PANEL ===
        mainPanel.add(new DashboardPanel(null), "HOME");
        mainPanel.add(new AddCompanyPanel(), "ADD_COMPANY");
        mainPanel.add(new ViewCompaniesForm(), "VIEW_COMPANIES");
        mainPanel.add(new CreateDriveForm(), "CREATE_DRIVE");
        mainPanel.add(new SearchStudentForm(), "SEARCH_STUDENT");
        mainPanel.add(new ViewApplicationsForm(), "APPLICATIONS");
        mainPanel.add(new NoticeBoardForm(true), "NOTICE_BOARD");
        mainPanel.add(new OfferManagementForm(), "OFFERS");
        mainPanel.add(new InterviewManagementForm(), "INTERVIEWS");
        mainPanel.add(new ResumeReviewForm(), "RESUMES");
        add(mainPanel, BorderLayout.CENTER);

        // === ANIMATED SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setBorder(new EmptyBorder(20, 12, 20, 12));
        sidebar.setPreferredSize(new Dimension(190, Integer.MAX_VALUE));

        String[][] navItems = {
            {"\uD83C\uDFE0", "Dashboard"},
            {"\uD83C\uDFE2", "Add Company"},
            {"\uD83D\uDCCB", "View Companies"},
            {"\uD83D\uDCC5", "Create Drive"},
            {"\uD83D\uDD0D", "Search Student"},
            {"\uD83D\uDCC2", "Applications"},
            {"\uD83D\uDCCC", "Notice Board"},
            {"\uD83D\uDCBC", "Offers"},
            {"\uD83C\uDFAF", "Interviews"},
            {"\uD83D\uDCC4", "Resumes"},
            {"\uD83D\uDD13", "Logout"}
        };

        String[] cardKeys = {
            "HOME", "ADD_COMPANY", "VIEW_COMPANIES", "CREATE_DRIVE",
            "SEARCH_STUDENT", "APPLICATIONS", "NOTICE_BOARD", "OFFERS",
            "INTERVIEWS", "RESUMES", "LOGOUT"
        };

        Color[] navColors = {
            new Color(99, 102, 241),  // Indigo - Dashboard
            new Color(16, 185, 129),  // Emerald - Add Company
            new Color(16, 185, 129),  // Emerald - View Companies
            new Color(245, 158, 11),  // Amber - Create Drive
            new Color(99, 102, 241),  // Indigo - Search Student
            new Color(239, 68, 68),   // Rose - Applications
            new Color(14, 165, 233),  // Sky - Notices
            new Color(168, 85, 247),  // Purple - Offers
            new Color(245, 158, 11),  // Amber - Interviews
            new Color(239, 68, 68),   // Rose - Resumes
            new Color(192, 57, 43),   // Red - Logout
        };

        for (int i = 0; i < navItems.length; i++) {
            final int index = i;
            String icon = navItems[i][0];
            String label = navItems[i][1];
            String key = cardKeys[i];
            Color color = navColors[i];

            JPanel navBtn = createNavButton(icon, label, color);
            navBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if ("LOGOUT".equals(key)) {
                        dispose();
                        new AdminLoginForm();
                    } else {
                        cardLayout.show(mainPanel, key);
                    }
                }
            });

            sidebar.add(navBtn);
            sidebar.add(Box.createVerticalStrut(6));
        }

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setBorder(BorderFactory.createEmptyBorder());
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sidebarScroll, BorderLayout.WEST);

        setVisible(true);
    }

    private JPanel createNavButton(String icon, String label, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setForeground(accentColor);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textLbl.setForeground(new Color(203, 213, 225));

        panel.add(iconLbl, BorderLayout.WEST);
        panel.add(textLbl, BorderLayout.CENTER);

        // Hover animation
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(51, 65, 85));
                textLbl.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(30, 41, 59));
                textLbl.setForeground(new Color(203, 213, 225));
            }
        });

        return panel;
    }
}