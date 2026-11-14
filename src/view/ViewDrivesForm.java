package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import dao.DriveDAO;
import util.UIConstants;
import java.sql.ResultSet;

public class ViewDrivesForm extends JPanel {

    private JPanel listPanel;

    public ViewDrivesForm() {
        setName("View Drives");
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(0, 15));

        // Header
        JLabel titleLabel = new JLabel("Scheduled Placement Drives");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Timeline of upcoming company drives, ordered by date");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        // Timeline list
        listPanel = new JPanel();
        listPanel.setBackground(new Color(245, 247, 250));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadDrivesData();
    }

    public void loadDrivesData() {
        listPanel.removeAll();

        Color[] accentColors = {
            new Color(99, 102, 241),  // Indigo
            new Color(16, 185, 129),  // Emerald
            new Color(245, 158, 11),  // Amber
            new Color(239, 68, 68),   // Red
            new Color(14, 165, 233),  // Sky
            new Color(168, 85, 247),  // Purple
        };
        int colorIndex = 0;

        try {
            DriveDAO dao = new DriveDAO();
            ResultSet rs = dao.getAllDrives();
            while (rs.next()) {
                int driveId = rs.getInt("drive_id");
                String company = rs.getString("company_name");
                String pkg = rs.getDouble("company_package") + " LPA";
                String date = rs.getString("drive_date");
                double cgpa = rs.getDouble("min_cgpa");
                Color accent = accentColors[colorIndex % accentColors.length];

                listPanel.add(createTimelineRow(driveId, company, pkg, date, cgpa, accent));
                listPanel.add(Box.createVerticalStrut(12));
                colorIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createTimelineRow(int id, String company, String pkg, String date, double cgpa, Color accent) {
        JPanel row = new JPanel(new BorderLayout(15, 0)) {
            private float shadowAlpha = 0f;
            private Timer timer;
            private boolean hovering = false;

            {
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(16, 0, 16, 20)
                ));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                timer = new Timer(15, e -> {
                    if (hovering && shadowAlpha < 1f) { shadowAlpha = Math.min(1f, shadowAlpha + 0.15f); repaint(); }
                    else if (!hovering && shadowAlpha > 0f) { shadowAlpha = Math.max(0f, shadowAlpha - 0.15f); repaint(); }
                    else { timer.stop(); }
                });

                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovering = true; timer.start(); }
                    @Override public void mouseExited(MouseEvent e) { hovering = false; timer.start(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (shadowAlpha > 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int alpha = (int)(shadowAlpha * 15);
                    g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), alpha));
                    g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                    g2.dispose();
                }
            }
        };

        // Colored left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(5, 0));

        // Dot indicator with circle
        JPanel dotPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                // Outer ring
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 50));
                g2.fillOval(cx - 14, cy - 14, 28, 28);
                // Inner dot
                g2.setColor(accent);
                g2.fillOval(cx - 8, cy - 8, 16, 16);
                g2.dispose();
            }
        };
        dotPanel.setOpaque(false);
        dotPanel.setPreferredSize(new Dimension(55, 0));

        // Content
        JPanel content = new JPanel(new GridLayout(2, 1, 0, 3));
        content.setOpaque(false);

        JLabel companyLbl = new JLabel(company);
        companyLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        companyLbl.setForeground(new Color(15, 23, 42));

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setOpaque(false);

        metaPanel.add(createBadge("📅 " + date, new Color(224, 231, 255), new Color(79, 70, 229)));
        metaPanel.add(Box.createHorizontalStrut(8));
        metaPanel.add(createBadge("Min CGPA: " + cgpa, new Color(220, 252, 231), new Color(22, 101, 52)));

        content.add(companyLbl);
        content.add(metaPanel);

        // Right-side package label
        JLabel pkgLabel = new JLabel(pkg);
        pkgLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pkgLabel.setForeground(accent);
        pkgLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pkgLabel.setPreferredSize(new Dimension(100, 0));

        row.add(accentBar, BorderLayout.WEST);
        row.add(dotPanel, BorderLayout.LINE_START);
        row.add(content, BorderLayout.CENTER);
        row.add(pkgLabel, BorderLayout.EAST);

        return row;
    }

    private JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(fg);
        badge.setBackground(bg);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }
}