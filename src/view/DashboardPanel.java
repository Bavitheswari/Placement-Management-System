package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import dao.StatisticsDAO;
import dao.DriveDAO;
import util.UIConstants;
import util.components.AnimatedCardPanel;
import util.components.AnimatedButton;

public class DashboardPanel extends JPanel {

    private final DashboardFrame parentFrame;
    private final StatisticsDAO statsDAO;
    private final DriveDAO driveDAO;

    public DashboardPanel(DashboardFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.statsDAO = new StatisticsDAO();
        this.driveDAO = new DriveDAO();

        setBackground(UIConstants.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // MAIN CONTENT SCROLLABLE PANELS
        JPanel mainContent = new JPanel();
        mainContent.setBackground(UIConstants.BACKGROUND);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        // WELCOME HEADER
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(20, 25, 20, 25)));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel welcome = new JLabel("Welcome Back, Administrator");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setForeground(UIConstants.TEXT_DARK);

        JLabel sub = new JLabel("Placement Management System Control Panel");
        sub.setForeground(UIConstants.TEXT_MUTED);
        sub.setFont(UIConstants.SUBTITLE_FONT);

        welcomePanel.add(welcome, BorderLayout.NORTH);
        welcomePanel.add(sub, BorderLayout.CENTER);

        // STATISTICS CARDS
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Retrieve real numbers from database
        String numStudents = String.valueOf(statsDAO.totalStudents());
        String numCompanies = String.valueOf(statsDAO.totalCompanies());
        String numDrives = String.valueOf(statsDAO.totalDrives());
        String numApplications = String.valueOf(statsDAO.totalApplications());

        cardsGrid.add(createStatCard("Total Students", numStudents, new Color(99, 102, 241))); // Indigo
        cardsGrid.add(createStatCard("Registered Companies", numCompanies, new Color(16, 185, 129))); // Emerald
        cardsGrid.add(createStatCard("Placement Drives", numDrives, new Color(245, 158, 11))); // Amber
        cardsGrid.add(createStatCard("Job Applications", numApplications, new Color(239, 68, 68))); // Rose

        // MIDDLE PANEL: QUICK ACTIONS & RECENT ACTIVITY
        JPanel middleRow = new JPanel(new GridLayout(1, 2, 25, 0));
        middleRow.setOpaque(false);
        middleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // QUICK ACTIONS
        JPanel actionsCard = new JPanel(new BorderLayout());
        actionsCard.setBackground(Color.WHITE);
        actionsCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel actionsTitle = new JLabel("Quick Controls");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionsTitle.setForeground(UIConstants.TEXT_DARK);
        actionsCard.add(actionsTitle, BorderLayout.NORTH);

        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        actionsGrid.setOpaque(false);
        actionsGrid.setBorder(new EmptyBorder(10, 0, 0, 0));

        AnimatedButton btnAddCompany = new AnimatedButton("Add New Company", new Color(16, 185, 129));
        AnimatedButton btnCreateDrive = new AnimatedButton("Create Hiring Drive", new Color(245, 158, 11));
        AnimatedButton btnSearchStudent = new AnimatedButton("Search Students", new Color(99, 102, 241));
        AnimatedButton btnViewApps = new AnimatedButton("Manage Applications", new Color(239, 68, 68));

        actionsGrid.add(btnAddCompany);
        actionsGrid.add(btnCreateDrive);
        actionsGrid.add(btnSearchStudent);
        actionsGrid.add(btnViewApps);
        actionsCard.add(actionsGrid, BorderLayout.CENTER);

        // RECENT ACTIVITY CARD
        JPanel activityCard = new JPanel(new BorderLayout());
        activityCard.setBackground(Color.WHITE);
        activityCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel activityTitle = new JLabel("System Log & Status");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setForeground(UIConstants.TEXT_DARK);
        activityCard.add(activityTitle, BorderLayout.NORTH);

        // Build log messages dynamically
        String logHtml = "<html><body style='font-family: Segoe UI; font-size: 13px; color: #475569; line-height: 1.5;'>"
                + "<table style='border-spacing: 0 8px;'>"
                + "<tr><td style='color: #4f46e5; font-weight: bold; padding-right: 10px;'>&bull;</td><td>Database Connected successfully.</td></tr>"
                + "<tr><td style='color: #10b981; font-weight: bold; padding-right: 10px;'>&bull;</td><td>" + numStudents
                + " Students registered in system.</td></tr>"
                + "<tr><td style='color: #f59e0b; font-weight: bold; padding-right: 10px;'>&bull;</td><td>" + numDrives
                + " placement drives active.</td></tr>"
                + "<tr><td style='color: #ef4444; font-weight: bold; padding-right: 10px;'>&bull;</td><td>" + numApplications
                + " total applications submitted.</td></tr>"
                + "</table></body></html>";

        JLabel activityLog = new JLabel(logHtml);
        activityLog.setBorder(new EmptyBorder(5, 5, 5, 5));
        activityCard.add(activityLog, BorderLayout.CENTER);

        middleRow.add(actionsCard);
        middleRow.add(activityCard);

        // REAL-TIME PLACEMENT DRIVES - ANIMATED CARDS
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(new Color(245, 247, 250));
        tableCard.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel tableTitle = new JLabel("Live Placement Drives");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(UIConstants.TEXT_DARK);
        tableTitle.setBorder(new EmptyBorder(0, 0, 5, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        JPanel drivesCardsPanel = new JPanel(new GridLayout(0, 2, 18, 18));
        drivesCardsPanel.setBackground(new Color(245, 247, 250));

        Color[] driveColors = { new Color(41,128,185), new Color(39,174,96), new Color(142,68,173), new Color(22,160,133), new Color(211,84,0) };
        int di = 0;
        try {
            ResultSet rs = driveDAO.getAllDrives();
            while (rs.next()) {
                Color color = driveColors[di % driveColors.length];
                AnimatedCardPanel dc = new AnimatedCardPanel(color);
                dc.setLayout(new BorderLayout());

                JPanel dcContent = new JPanel();
                dcContent.setLayout(new BoxLayout(dcContent, BoxLayout.Y_AXIS));
                dcContent.setOpaque(false);

                JLabel compName = new JLabel(rs.getString("company_name"));
                compName.setFont(new Font("Segoe UI", Font.BOLD, 16));
                compName.setForeground(Color.WHITE);
                compName.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel compPkg = new JLabel(rs.getDouble("company_package") + " LPA");
                compPkg.setFont(new Font("Segoe UI", Font.BOLD, 14));
                compPkg.setForeground(new Color(255, 235, 132));
                compPkg.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel compDate = new JLabel("Date: " + rs.getString("drive_date"));
                compDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                compDate.setForeground(new Color(220, 220, 220));
                compDate.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel compCgpa = new JLabel("Min CGPA: " + rs.getDouble("min_cgpa"));
                compCgpa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                compCgpa.setForeground(new Color(220, 220, 220));
                compCgpa.setAlignmentX(Component.LEFT_ALIGNMENT);

                dcContent.add(compName); dcContent.add(Box.createVerticalStrut(6));
                dcContent.add(compPkg); dcContent.add(Box.createVerticalStrut(4));
                dcContent.add(compDate); dcContent.add(Box.createVerticalStrut(4));
                dcContent.add(compCgpa);
                dc.add(dcContent, BorderLayout.CENTER);
                drivesCardsPanel.add(dc);
                di++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(drivesCardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Wire Up Quick Action listeners
        btnAddCompany.addActionListener(e -> parentFrame.showTab("COMPANIES"));
        btnCreateDrive.addActionListener(e -> parentFrame.showTab("DRIVES"));
        btnSearchStudent.addActionListener(e -> parentFrame.showTab("STUDENTS"));
        btnViewApps.addActionListener(e -> parentFrame.showTab("APPLICATIONS"));

        // Compile Main Dashboard Layout
        mainContent.add(welcomePanel);
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(cardsGrid);
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(middleRow);
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(tableCard);

        add(mainContent, BorderLayout.CENTER);
    }

    private AnimatedCardPanel createStatCard(String title, String value, Color accentColor) {
        AnimatedCardPanel card = new AnimatedCardPanel(accentColor);
        card.setLayout(new BorderLayout(0, 5));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(230, 230, 230));

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblVal.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.CENTER);
        card.add(lblVal, BorderLayout.SOUTH);

        return card;
    }


}
