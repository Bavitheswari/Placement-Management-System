package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import util.UIConstants;

public class DashboardFrame extends JFrame {

    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private JButton activeButton;

    public DashboardFrame() {
        setTitle("Admin Console - Placement Management System");
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UIConstants.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel brandLabel = new JLabel("Admin Panel");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(new EmptyBorder(10, 0, 30, 0));
        sidebar.add(brandLabel);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Sidebar Buttons
        JButton btnDashboard = createSidebarButton("Dashboard", "DASHBOARD");
        JButton btnStudents = createSidebarButton("Students", "STUDENTS");
        JButton btnCompanies = createSidebarButton("Companies", "COMPANIES");
        JButton btnDrives = createSidebarButton("Drives", "DRIVES");
        JButton btnApplications = createSidebarButton("Applications", "APPLICATIONS");
        JButton btnNotices = createSidebarButton("Notice Board", "NOTICES");
        JButton btnOffers = createSidebarButton("Offers", "OFFERS");
        JButton btnInterviews = createSidebarButton("Interviews", "INTERVIEWS");
        JButton btnAnalytics = createSidebarButton("Analytics", "ANALYTICS");

        JButton btnLogout = new JButton("Logout");
        btnLogout.setForeground(new Color(248, 113, 113)); // Soft red
        btnLogout.setBackground(UIConstants.SIDEBAR);
        btnLogout.setFont(UIConstants.BOLD_FONT);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnLogout.addActionListener(e -> {
            dispose();
            new HomePage();
        });

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnStudents);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnCompanies);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnDrives);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnApplications);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnNotices);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnOffers);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnInterviews);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnAnalytics);
        sidebar.add(Box.createGlue()); // Push logout to bottom
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // TOP BAR
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.HEADER);
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel title = new JLabel("Placement Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel adminLabel = new JLabel("Logged in as: Administrator");
        adminLabel.setForeground(new Color(203, 213, 225));
        adminLabel.setFont(UIConstants.BOLD_FONT);

        header.add(title, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // INSTANTIATING REAL VIEW PANELS
        SearchStudentForm searchStudentPanel = new SearchStudentForm();
        ViewApplicationsForm viewAppsPanel = new ViewApplicationsForm();
        AnalyticsChartPanel analyticsPanel = new AnalyticsChartPanel();

        // Custom JTabbedPanes for Companies and Drives sections
        JTabbedPane companiesTabbedPane = new JTabbedPane();
        companiesTabbedPane.addTab("View Companies", new ViewCompaniesForm());
        companiesTabbedPane.addTab("Add Company", new AddCompanyPanel());

        JTabbedPane drivesTabbedPane = new JTabbedPane();
        drivesTabbedPane.addTab("View Placement Drives", new ViewDrivesForm());
        drivesTabbedPane.addTab("Create New Drive", new CreateDriveForm());

        // Add real panels to main CardLayout container
        contentPanel.add(new DashboardPanel(this), "DASHBOARD");
        contentPanel.add(searchStudentPanel, "STUDENTS");
        contentPanel.add(companiesTabbedPane, "COMPANIES");
        contentPanel.add(drivesTabbedPane, "DRIVES");
        contentPanel.add(viewAppsPanel, "APPLICATIONS");
        contentPanel.add(new NoticeBoardForm(true), "NOTICES");
        contentPanel.add(new OfferManagementForm(), "OFFERS");
        contentPanel.add(new InterviewManagementForm(), "INTERVIEWS");
        contentPanel.add(analyticsPanel, "ANALYTICS");

        add(contentPanel, BorderLayout.CENTER);

        // Initialize active state
        setActiveButton(btnDashboard);
        setVisible(true);
    }

    public void showTab(String tabName) {
        cardLayout.show(contentPanel, tabName);

        // Synchronize sidebar button highlight with programmatic tab switches
        Component[] components = getComponents();
        for (Component c : components) {
            if (c instanceof JPanel && c.getBackground().equals(UIConstants.SIDEBAR)) {
                Component[] buttons = ((JPanel) c).getComponents();
                for (Component b : buttons) {
                    if (b instanceof JButton && tabName.equals(((JButton) b).getClientProperty("tabName"))) {
                        setActiveButton((JButton) b);
                    }
                }
            }
        }
    }

    private void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(UIConstants.SIDEBAR);
            activeButton.setForeground(new Color(156, 163, 175)); // Griseous text
        }
        activeButton = button;
        activeButton.setBackground(UIConstants.BUTTON);
        activeButton.setForeground(Color.WHITE);
    }

    private JButton createSidebarButton(String text, String tabName) {
        JButton button = new JButton(text);
        button.putClientProperty("tabName", tabName);
        button.setForeground(new Color(156, 163, 175));
        button.setBackground(UIConstants.SIDEBAR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(new Color(30, 41, 59)); // Slate 800
                    button.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(UIConstants.SIDEBAR);
                    button.setForeground(new Color(156, 163, 175));
                }
            }
        });

        button.addActionListener(e -> {
            setActiveButton(button);
            cardLayout.show(contentPanel, tabName);
        });

        return button;
    }
}
