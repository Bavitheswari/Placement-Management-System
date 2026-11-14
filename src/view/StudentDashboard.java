package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dao.StudentDAO;
import model.Student;
import util.DBConnection;
import util.UIConstants;
import util.components.AnimatedCardPanel;

public class StudentDashboard extends JFrame {

    private final String email;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private JPanel activeSidebarItem;

    // Dashboard Components (to reload on show)
    private JLabel lblResumeStatus;
    private JPanel alertCard;
    private JLabel alertTitle;
    private JLabel lblEligibleDrivesCount;
    private JLabel lblAppliedDrivesCount;
    private JLabel lblPlacementStatusVal;
    private JTable tblEligibleDrives;
    private JTable tblApplications;

    public StudentDashboard(String studentName, String email) {
        this.email = email;

        setTitle("Student Portal - Career Placement Management");
        setSize(1300, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === DARK 3D SIDEBAR ===
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(7, 11, 23), 0, getHeight(), new Color(17, 26, 46));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));
        sidebar.setOpaque(false);

        // Sidebar brand header
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(25, 15, 20, 15));
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel brandIcon = new JLabel("🎓");
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        brandIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandTitle = new JLabel("Student Portal");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandSub = new JLabel(studentName);
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSub.setForeground(new Color(100, 140, 200));
        brandSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(brandIcon);
        brandPanel.add(Box.createVerticalStrut(6));
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(brandSub);
        sidebar.add(brandPanel);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(40, 60, 90));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(15));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // === NAV ITEMS ===
        String[][] navItems = {
            {"\uD83C\uDFE0", "Dashboard Home", "HOME"},
            {"\uD83D\uDC64", "Update Profile", "UPDATE_PROFILE"},
            {"\uD83D\uDCC4", "Upload Resume", "UPLOAD_RESUME"},
            {"\uD83D\uDCBC", "Placement Drives", "DRIVES"},
            {"\u270D\uFE0F", "Apply For Drive", "APPLY"},
            {"\uD83D\uDCCB", "My Applications", "APPLICATIONS"},
            {"\uD83D\uDCCC", "Notice Board", "NOTICES"},
            {"\uD83C\uDFC6", "My Offers", "OFFERS"},
            {"\uD83D\uDD11", "Change Password", "PASSWORD"}
        };

        Color[] accentColors = {
            new Color(99, 102, 241),   // Indigo
            new Color(16, 185, 129),   // Emerald
            new Color(14, 165, 233),   // Sky
            new Color(245, 158, 11),   // Amber
            new Color(239, 68, 68),    // Red
            new Color(168, 85, 247),   // Purple
            new Color(236, 72, 153),   // Pink
            new Color(34, 197, 94),    // Green
            new Color(249, 115, 22),   // Orange
        };

        for (int i = 0; i < navItems.length; i++) {
            final int idx = i;
            String icon = navItems[i][0];
            String label = navItems[i][1];
            String key = navItems[i][2];
            Color accent = accentColors[i];

            JPanel navItem = createNavItem(icon, label, accent);
            if (i == 0) activeSidebarItem = navItem;

            navItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setActiveNavItem(navItem, accent);
                    if ("HOME".equals(key)) loadProfileData();
                    cardLayout.show(contentPanel, key);
                }
            });
            sidebar.add(navItem);
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JPanel logoutItem = createNavItem("\uD83D\uDD13", "Logout", new Color(239, 68, 68));
        logoutItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new HomePage();
            }
        });
        sidebar.add(logoutItem);
        sidebar.add(Box.createVerticalStrut(15));

        add(sidebar, BorderLayout.WEST);

        // === GRADIENT HEADER BAR ===
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 58, 138), getWidth(), 0, new Color(79, 70, 229));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 68));
        header.setBorder(new EmptyBorder(0, 25, 0, 25));
        header.setLayout(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Workspace");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel rightHeader = new JPanel(new BorderLayout(5, 0));
        rightHeader.setOpaque(false);
        JLabel studentLabel = new JLabel("Welcome, " + studentName + " 👋");
        studentLabel.setForeground(new Color(199, 210, 254));
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightHeader.add(studentLabel, BorderLayout.CENTER);

        header.add(title, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === ADD PANELS ===
        JPanel homePanel = createHomePanel(studentName);
        UpdateProfileForm updatePanel = new UpdateProfileForm();
        UploadResumeForm uploadPanel = new UploadResumeForm();
        ApplyDriveForm applyFormPanel = new ApplyDriveForm();
        ViewMyApplicationsForm myAppsPanel = new ViewMyApplicationsForm(email);
        ChangePasswordForm passwordPanel = new ChangePasswordForm();

        JTabbedPane drivesTabbedPane = new JTabbedPane();
        drivesTabbedPane.addTab("Eligible Drives", new EligibleDrivesForm());
        drivesTabbedPane.addTab("All Active Drives", new ViewDrivesForm());

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(updatePanel, "UPDATE_PROFILE");
        contentPanel.add(uploadPanel, "UPLOAD_RESUME");
        contentPanel.add(drivesTabbedPane, "DRIVES");
        contentPanel.add(applyFormPanel, "APPLY");
        contentPanel.add(myAppsPanel, "APPLICATIONS");
        contentPanel.add(new NoticeBoardForm(false), "NOTICES");

        try {
            StudentDAO sDao = new StudentDAO();
            model.Student s = sDao.getStudentByEmail(email);
            if (s != null) {
                contentPanel.add(new StudentOfferForm(s.getStudentId()), "OFFERS");
            }
        } catch (Exception e) {}

        contentPanel.add(passwordPanel, "PASSWORD");
        add(contentPanel, BorderLayout.CENTER);

        // Highlight first item
        setActiveNavItem(activeSidebarItem, accentColors[0]);
        setVisible(true);
    }

    private JPanel createNavItem(String icon, String label, Color accent) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(10, 15, 10, 15));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setPreferredSize(new Dimension(28, 28));

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textLbl.setForeground(new Color(148, 163, 184));

        // Left accent strip
        JPanel accentBar = new JPanel();
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(3, 0));
        item.putClientProperty("accent", accent);
        item.putClientProperty("textLbl", textLbl);
        item.putClientProperty("accentBar", accentBar);

        item.add(accentBar, BorderLayout.WEST);
        item.add(iconLbl, BorderLayout.CENTER);

        JPanel labelWrap = new JPanel(new BorderLayout());
        labelWrap.setOpaque(false);
        labelWrap.add(textLbl, BorderLayout.WEST);

        item.add(iconLbl, BorderLayout.WEST);
        item.add(labelWrap, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != activeSidebarItem) {
                    item.setOpaque(true);
                    item.setBackground(new Color(20, 30, 50));
                    textLbl.setForeground(Color.WHITE);
                    item.repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (item != activeSidebarItem) {
                    item.setOpaque(false);
                    textLbl.setForeground(new Color(148, 163, 184));
                    item.repaint();
                }
            }
        });

        return item;
    }

    private void setActiveNavItem(JPanel item, Color accent) {
        // Reset old
        if (activeSidebarItem != null) {
            activeSidebarItem.setOpaque(false);
            JLabel oldText = (JLabel) activeSidebarItem.getClientProperty("textLbl");
            if (oldText != null) oldText.setForeground(new Color(148, 163, 184));
            JPanel oldBar = (JPanel) activeSidebarItem.getClientProperty("accentBar");
            if (oldBar != null) oldBar.setOpaque(false);
            activeSidebarItem.repaint();
        }

        activeSidebarItem = item;
        item.setOpaque(true);
        item.setBackground(new Color(15, 30, 60));
        JLabel text = (JLabel) item.getClientProperty("textLbl");
        if (text != null) text.setForeground(Color.WHITE);
        JPanel bar = (JPanel) item.getClientProperty("accentBar");
        if (bar != null) { bar.setOpaque(true); bar.setBackground(accent); }
        item.repaint();
    }

    private JPanel createHomePanel(String studentName) {
        JPanel home = new JPanel();
        home.setBackground(new Color(245, 247, 250));
        home.setLayout(new BorderLayout(20, 20));
        home.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Top section
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // === GLASSMORPHISM WELCOME BANNER ===
        JPanel welcomeCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 58, 138), getWidth(), 0, new Color(79, 70, 229));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                // Glass sheen
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 15, 15);
                g2.dispose();
            }
        };
        welcomeCard.setOpaque(false);
        welcomeCard.setLayout(new BorderLayout(0, 5));
        welcomeCard.setBorder(new EmptyBorder(22, 28, 22, 28));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel welcome = new JLabel("Welcome Back, " + studentName + " 🎓");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Track your eligibility, schedule interviews, and manage resumes — all in one place.");
        sub.setForeground(new Color(199, 210, 254));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        welcomeCard.add(welcome, BorderLayout.NORTH);
        welcomeCard.add(sub, BorderLayout.CENTER);

        // === ALERT CARD (status warning) ===
        alertCard = new JPanel(new BorderLayout(10, 0));
        alertCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        alertCard.setBorder(new EmptyBorder(12, 18, 12, 18));
        alertCard.setBackground(new Color(254, 243, 199));

        alertTitle = new JLabel("⚠ Placement Eligibility Status");
        alertTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblResumeStatus = new JLabel("Checking Resume Submission...");
        lblResumeStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        alertCard.add(alertTitle, BorderLayout.NORTH);
        alertCard.add(lblResumeStatus, BorderLayout.CENTER);

        headerPanel.add(welcomeCard);
        headerPanel.add(Box.createVerticalStrut(14));
        headerPanel.add(alertCard);
        home.add(headerPanel, BorderLayout.NORTH);

        // === 3D ANIMATED STAT CARDS ===
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 130));

        AnimatedCardPanel cardEligible = new AnimatedCardPanel(new Color(99, 102, 241));
        cardEligible.setLayout(new BorderLayout(0, 6));
        JLabel lblEligTitle = new JLabel("Eligible Drives");
        lblEligTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEligTitle.setForeground(new Color(220, 220, 255));
        lblEligibleDrivesCount = new JLabel("0");
        lblEligibleDrivesCount.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblEligibleDrivesCount.setForeground(Color.WHITE);
        cardEligible.add(lblEligTitle, BorderLayout.NORTH);
        cardEligible.add(lblEligibleDrivesCount, BorderLayout.CENTER);

        AnimatedCardPanel cardApplied = new AnimatedCardPanel(new Color(16, 185, 129));
        cardApplied.setLayout(new BorderLayout(0, 6));
        JLabel lblApplTitle = new JLabel("Applied Drives");
        lblApplTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblApplTitle.setForeground(new Color(200, 255, 230));
        lblAppliedDrivesCount = new JLabel("0");
        lblAppliedDrivesCount.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblAppliedDrivesCount.setForeground(Color.WHITE);
        cardApplied.add(lblApplTitle, BorderLayout.NORTH);
        cardApplied.add(lblAppliedDrivesCount, BorderLayout.CENTER);

        AnimatedCardPanel cardStatus = new AnimatedCardPanel(new Color(245, 158, 11));
        cardStatus.setLayout(new BorderLayout(0, 6));
        JLabel lblStatTitle = new JLabel("Placement Status");
        lblStatTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatTitle.setForeground(new Color(255, 243, 200));
        lblPlacementStatusVal = new JLabel("ACTIVE");
        lblPlacementStatusVal.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblPlacementStatusVal.setForeground(Color.WHITE);
        cardStatus.add(lblStatTitle, BorderLayout.NORTH);
        cardStatus.add(lblPlacementStatusVal, BorderLayout.CENTER);

        statsPanel.add(cardEligible);
        statsPanel.add(cardApplied);
        statsPanel.add(cardStatus);

        // === CENTER: TABLE COLLAGE ===
        JPanel collagePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        collagePanel.setOpaque(false);

        // Left Table: Eligible Drives - styled table
        JPanel eligiblePanel = buildStyledTablePanel("📅 Upcoming Eligible Drives", new Color(99, 102, 241));
        tblEligibleDrives = new JTable();
        styleTable(tblEligibleDrives);
        JScrollPane scrollEligible = new JScrollPane(tblEligibleDrives);
        scrollEligible.setBorder(BorderFactory.createEmptyBorder());
        scrollEligible.getViewport().setBackground(Color.WHITE);
        eligiblePanel.add(scrollEligible, BorderLayout.CENTER);

        // Right Table: My Applications
        JPanel appsPanel = buildStyledTablePanel("📂 My Application History", new Color(16, 185, 129));
        tblApplications = new JTable();
        styleTable(tblApplications);
        JScrollPane scrollApps = new JScrollPane(tblApplications);
        scrollApps.setBorder(BorderFactory.createEmptyBorder());
        scrollApps.getViewport().setBackground(Color.WHITE);
        appsPanel.add(scrollApps, BorderLayout.CENTER);

        collagePanel.add(eligiblePanel);
        collagePanel.add(appsPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(collagePanel, BorderLayout.CENTER);

        home.add(centerPanel, BorderLayout.CENTER);
        loadProfileData();
        return home;
    }

    private JPanel buildStyledTablePanel(String title, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1, true),
            new EmptyBorder(0, 0, 10, 0)
        ));

        // Colored header strip
        JPanel headerStrip = new JPanel(new BorderLayout());
        headerStrip.setBackground(accentColor);
        headerStrip.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        headerStrip.add(lbl);
        panel.add(headerStrip, BorderLayout.NORTH);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(55, 65, 81));
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(224, 231, 255));
    }

    private void loadProfileData() {
        try {
            StudentDAO studentDao = new StudentDAO();
            Student student = studentDao.getStudentByEmail(email);

            if (student != null) {
                int studentId = student.getStudentId();
                double cgpa = student.getCgpa();

                String path = studentDao.getResumePathByStudentId(studentId);
                if (path == null || path.isEmpty()) {
                    alertCard.setBackground(new Color(254, 226, 226));
                    alertTitle.setText("⛔ Action Required: No Resume Uploaded");
                    alertTitle.setForeground(new Color(153, 27, 27));
                    lblResumeStatus.setText("Please upload your resume in PDF/Word format to begin applying for placement drives.");
                    lblResumeStatus.setForeground(new Color(185, 28, 28));
                } else {
                    alertCard.setBackground(new Color(220, 252, 231));
                    alertTitle.setText("✅ Ready: Resume on File");
                    alertTitle.setForeground(new Color(21, 128, 61));
                    lblResumeStatus.setText("Your resume is uploaded. You can now apply for eligible placement drives.");
                    lblResumeStatus.setForeground(new Color(22, 101, 52));
                }

                Connection con = DBConnection.getConnection();

                PreparedStatement ps1 = con.prepareStatement("SELECT COUNT(*) FROM drive WHERE min_cgpa <= ?");
                ps1.setDouble(1, cgpa);
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) lblEligibleDrivesCount.setText(String.valueOf(rs1.getInt(1)));

                PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) FROM application WHERE student_id = ?");
                ps2.setInt(1, studentId);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) lblAppliedDrivesCount.setText(String.valueOf(rs2.getInt(1)));

                PreparedStatement ps3 = con.prepareStatement("SELECT COUNT(*) FROM application WHERE student_id = ? AND status = 'Approved'");
                ps3.setInt(1, studentId);
                ResultSet rs3 = ps3.executeQuery();
                if (rs3.next() && rs3.getInt(1) > 0) {
                    lblPlacementStatusVal.setText("PLACED ✓");
                } else {
                    lblPlacementStatusVal.setText("ACTIVE");
                }

                PreparedStatement ps4 = con.prepareStatement(
                    "SELECT c.company_name, d.drive_date, d.min_cgpa FROM drive d " +
                    "JOIN company c ON d.company_id = c.company_id WHERE d.min_cgpa <= ? ORDER BY d.drive_date DESC LIMIT 5");
                ps4.setDouble(1, cgpa);
                tblEligibleDrives.setModel(buildTableModel(ps4.executeQuery(), new String[]{"Company", "Drive Date", "Min CGPA"}));

                PreparedStatement ps5 = con.prepareStatement(
                    "SELECT c.company_name, a.status FROM application a " +
                    "JOIN drive d ON a.drive_id = d.drive_id JOIN company c ON d.company_id = c.company_id " +
                    "WHERE a.student_id = ? ORDER BY a.application_id DESC LIMIT 5");
                ps5.setInt(1, studentId);
                tblApplications.setModel(buildTableModel(ps5.executeQuery(), new String[]{"Company", "Status"}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private javax.swing.table.DefaultTableModel buildTableModel(ResultSet rs, String[] columnNames) throws java.sql.SQLException {
        java.util.Vector<String> columns = new java.util.Vector<>();
        for (String name : columnNames) columns.add(name);
        java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
        while (rs.next()) {
            java.util.Vector<Object> row = new java.util.Vector<>();
            for (int i = 1; i <= columnNames.length; i++) row.add(rs.getObject(i));
            data.add(row);
        }
        return new javax.swing.table.DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }
}