package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class HomePage extends JFrame {

    // Floating shape data for the animated background
    private final ArrayList<float[]> shapes = new ArrayList<>();
    private final Random rand = new Random(42);
    private Timer animationTimer;

    // Hover animation progress for cards (0.0 to 1.0)
    private float studentHover = 0f;
    private float adminHover = 0f;
    private Timer studentHoverTimer;
    private Timer adminHoverTimer;

    // Counter animation
    private int animatedCompanies = 0;
    private int animatedStudents = 0;
    private int animatedDrives = 0;
    private int animatedRate = 0;
    private final int TARGET_COMPANIES = 150;
    private final int TARGET_STUDENTS = 2500;
    private final int TARGET_DRIVES = 320;
    private final int TARGET_RATE = 95;
    private Timer counterTimer;
    private boolean countersStarted = false;

    public HomePage() {
        setTitle("Placement Management System");
        setSize(1100, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 700));

        // Initialize floating shapes: [x, y, radius, speedX, speedY, opacity, type]
        for (int i = 0; i < 18; i++) {
            shapes.add(new float[] {
                    rand.nextFloat() * 1200,
                    rand.nextFloat() * 900,
                    20 + rand.nextFloat() * 60,
                    (rand.nextFloat() - 0.5f) * 0.8f,
                    (rand.nextFloat() - 0.5f) * 0.6f,
                    0.04f + rand.nextFloat() * 0.08f,
                    rand.nextInt(3) // 0=circle, 1=diamond, 2=ring
            });
        }

        // The entire content sits on a custom-painted background panel
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        backgroundPanel.setBackground(new Color(15, 23, 42));

        // Scrollable content
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // ===== HERO SECTION =====
        JPanel heroPanel = createHeroSection();
        contentPanel.add(heroPanel);

        // ===== STATS SECTION =====
        JPanel statsPanel = createStatsSection();
        contentPanel.add(statsPanel);

        // ===== PORTAL CARDS SECTION =====
        JPanel portalSection = createPortalSection();
        contentPanel.add(portalSection);

        // ===== FEATURES SECTION =====
        JPanel featuresPanel = createFeaturesSection();
        contentPanel.add(featuresPanel);

        // ===== FOOTER =====
        JPanel footerPanel = createFooter();
        contentPanel.add(footerPanel);

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(backgroundPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(15, 23, 42));
        add(scrollPane);

        // Start background animation
        animationTimer = new Timer(40, e -> {
            for (float[] s : shapes) {
                s[0] += s[3];
                s[1] += s[4];
                if (s[0] < -80)
                    s[0] = getWidth() + 80;
                if (s[0] > getWidth() + 80)
                    s[0] = -80;
                if (s[1] < -80)
                    s[1] = getHeight() + 80;
                if (s[1] > getHeight() + 80)
                    s[1] = -80;
            }
            backgroundPanel.repaint();
        });
        animationTimer.start();

        // Start counter animation after a short delay
        counterTimer = new Timer(20, e -> {
            boolean allDone = true;
            if (animatedCompanies < TARGET_COMPANIES) {
                animatedCompanies += 2;
                allDone = false;
            }
            if (animatedStudents < TARGET_STUDENTS) {
                animatedStudents += 35;
                allDone = false;
            }
            if (animatedDrives < TARGET_DRIVES) {
                animatedDrives += 4;
                allDone = false;
            }
            if (animatedRate < TARGET_RATE) {
                animatedRate += 1;
                allDone = false;
            }
            // Clamp
            animatedCompanies = Math.min(animatedCompanies, TARGET_COMPANIES);
            animatedStudents = Math.min(animatedStudents, TARGET_STUDENTS);
            animatedDrives = Math.min(animatedDrives, TARGET_DRIVES);
            animatedRate = Math.min(animatedRate, TARGET_RATE);
            backgroundPanel.repaint();
            if (allDone)
                ((Timer) e.getSource()).stop();
        });
        Timer delayTimer = new Timer(600, e -> {
            counterTimer.start();
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();

        // Clean up timers on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (animationTimer != null)
                    animationTimer.stop();
                if (counterTimer != null)
                    counterTimer.stop();
                if (studentHoverTimer != null)
                    studentHoverTimer.stop();
                if (adminHoverTimer != null)
                    adminHoverTimer.stop();
            }
        });

        setVisible(true);
    }

    // ======================== BACKGROUND PAINTING ========================
    private void paintBackground(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Deep gradient: dark navy to indigo
        GradientPaint bgGrad = new GradientPaint(0, 0, new Color(15, 23, 42),
                w, h, new Color(30, 27, 75));
        g2.setPaint(bgGrad);
        g2.fillRect(0, 0, w, h);

        // Radial glow at top-right
        RadialGradientPaint glow = new RadialGradientPaint(
                new Point2D.Float(w * 0.8f, h * 0.15f),
                Math.max(w, h) * 0.4f,
                new float[] { 0f, 1f },
                new Color[] { new Color(99, 102, 241, 30), new Color(99, 102, 241, 0) });
        g2.setPaint(glow);
        g2.fillRect(0, 0, w, h);

        // Radial glow at bottom-left
        RadialGradientPaint glow2 = new RadialGradientPaint(
                new Point2D.Float(w * 0.15f, h * 0.85f),
                Math.max(w, h) * 0.35f,
                new float[] { 0f, 1f },
                new Color[] { new Color(139, 92, 246, 25), new Color(139, 92, 246, 0) });
        g2.setPaint(glow2);
        g2.fillRect(0, 0, w, h);

        // Draw floating shapes
        for (float[] s : shapes) {
            int alpha = (int) (s[5] * 255);
            g2.setColor(new Color(165, 180, 252, alpha)); // Indigo 300
            int type = (int) s[6];
            float cx = s[0], cy = s[1], r = s[2];
            if (type == 0) {
                // Filled circle
                g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
            } else if (type == 1) {
                // Diamond
                Path2D diamond = new Path2D.Float();
                diamond.moveTo(cx, cy - r);
                diamond.lineTo(cx + r * 0.7, cy);
                diamond.lineTo(cx, cy + r);
                diamond.lineTo(cx - r * 0.7, cy);
                diamond.closePath();
                g2.fill(diamond);
            } else {
                // Ring
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
            }
        }
    }

    // ======================== HERO SECTION ========================
    private JPanel createHeroSection() {
        JPanel hero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Fully transparent — background shows through
            }
        };
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBorder(new EmptyBorder(60, 60, 30, 60));

        // University branding badge
        JLabel badge = new JLabel("    CAMPUS PLACEMENT PORTAL    ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(99, 102, 241, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(99, 102, 241, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(165, 180, 252)); // Indigo 300
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(new EmptyBorder(6, 16, 6, 16));
        badge.setOpaque(false);

        hero.add(badge);
        hero.add(Box.createVerticalStrut(20));

        // Main title
        JLabel title = new JLabel("Placement Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 44));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.add(title);

        JLabel title2 = new JLabel("System");
        title2.setFont(new Font("Segoe UI", Font.BOLD, 44));
        title2.setForeground(new Color(165, 180, 252)); // Indigo 300
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.add(title2);

        hero.add(Box.createVerticalStrut(16));

        // Tagline
        JLabel tagline = new JLabel("Bridging Academic Excellence with Industry Opportunities");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        tagline.setForeground(new Color(148, 163, 184)); // Slate 400
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.add(tagline);

        hero.add(Box.createVerticalStrut(8));

        JLabel tagline2 = new JLabel(
                "Streamline campus recruitment \u2022 Track applications \u2022 Achieve career goals");
        tagline2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline2.setForeground(new Color(100, 116, 139)); // Slate 500
        tagline2.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.add(tagline2);

        return hero;
    }

    // ======================== STATS SECTION ========================
    private JPanel createStatsSection() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(20, 80, 20, 80));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Horizontal stats bar
        JPanel statsBar = new JPanel(new GridLayout(1, 4, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glassmorphic background
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        statsBar.setOpaque(false);
        statsBar.setBorder(new EmptyBorder(20, 20, 20, 20));

        statsBar.add(createStatItem("COMPANIES", () -> String.valueOf(animatedCompanies) + "+"));
        statsBar.add(createStatItem("STUDENTS", () -> String.valueOf(animatedStudents) + "+"));
        statsBar.add(createStatItem("DRIVES", () -> String.valueOf(animatedDrives) + "+"));
        statsBar.add(createStatItem("SUCCESS RATE", () -> animatedRate + "%"));

        wrapper.add(statsBar);
        return wrapper;
    }

    private JPanel createStatItem(String label, java.util.function.Supplier<String> valueSupplier) {
        JPanel item = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Transparent
            }

            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                String val = valueSupplier.get();
                Font numFont = new Font("Segoe UI", Font.BOLD, 28);
                Font lblFont = new Font("Segoe UI", Font.PLAIN, 11);

                FontMetrics fmNum = g2.getFontMetrics(numFont);
                FontMetrics fmLbl = g2.getFontMetrics(lblFont);

                int totalH = fmNum.getHeight() + 4 + fmLbl.getHeight();
                int startY = (getHeight() - totalH) / 2 + fmNum.getAscent();

                // Number
                g2.setFont(numFont);
                g2.setColor(Color.WHITE);
                int numW = fmNum.stringWidth(val);
                g2.drawString(val, (getWidth() - numW) / 2, startY);

                // Label
                g2.setFont(lblFont);
                g2.setColor(new Color(148, 163, 184));
                int lblW = fmLbl.stringWidth(label);
                g2.drawString(label, (getWidth() - lblW) / 2, startY + fmNum.getDescent() + 4 + fmLbl.getAscent());

                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(180, 70));
        return item;
    }

    // ======================== PORTAL SECTION ========================
    private JPanel createPortalSection() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Section heading
        JLabel heading = new JLabel("Select Your Portal");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(heading);
        wrapper.add(Box.createVerticalStrut(6));

        JLabel subheading = new JLabel("Choose your role to access the placement portal");
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subheading.setForeground(new Color(148, 163, 184));
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(subheading);
        wrapper.add(Box.createVerticalStrut(30));

        // Cards container
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(0, 100, 0, 100));
        cardsPanel.setMaximumSize(new Dimension(900, 300));

        JPanel studentCard = createPortalCard(
                "Student Portal",
                "Access drives, upload resumes, track your applications, and explore career opportunities.",
                new String[] { "View Eligible Drives", "Upload Resume", "Track Applications" },
                new Color(99, 102, 241), // Indigo
                new Color(79, 70, 229),
                0 // icon type: graduation cap
        );

        JPanel adminCard = createPortalCard(
                "Admin Portal",
                "Manage students, company drives, review applications, and monitor placement analytics.",
                new String[] { "Manage Students", "Create Drives", "View Analytics" },
                new Color(139, 92, 246), // Violet
                new Color(124, 58, 237),
                1 // icon type: shield
        );

        // Hover animation for student card
        studentHoverTimer = new Timer(16, null);
        studentCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                studentHoverTimer.stop();
                studentHoverTimer = new Timer(16, ev -> {
                    studentHover = Math.min(1f, studentHover + 0.08f);
                    studentCard.repaint();
                    if (studentHover >= 1f)
                        studentHoverTimer.stop();
                });
                studentHoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                studentHoverTimer.stop();
                studentHoverTimer = new Timer(16, ev -> {
                    studentHover = Math.max(0f, studentHover - 0.08f);
                    studentCard.repaint();
                    if (studentHover <= 0f)
                        studentHoverTimer.stop();
                });
                studentHoverTimer.start();
            }
        });

        // Hover animation for admin card
        adminHoverTimer = new Timer(16, null);
        adminCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                adminHoverTimer.stop();
                adminHoverTimer = new Timer(16, ev -> {
                    adminHover = Math.min(1f, adminHover + 0.08f);
                    adminCard.repaint();
                    if (adminHover >= 1f)
                        adminHoverTimer.stop();
                });
                adminHoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                adminHoverTimer.stop();
                adminHoverTimer = new Timer(16, ev -> {
                    adminHover = Math.max(0f, adminHover - 0.08f);
                    adminCard.repaint();
                    if (adminHover <= 0f)
                        adminHoverTimer.stop();
                });
                adminHoverTimer.start();
            }
        });

        // Click handlers
        studentCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new StudentLoginForm();
            }
        });

        adminCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new AdminLoginForm();
            }
        });

        cardsPanel.add(studentCard);
        cardsPanel.add(adminCard);
        wrapper.add(cardsPanel);

        return wrapper;
    }

    private JPanel createPortalCard(String title, String desc, String[] features,
            Color accentColor, Color accentDark, int iconType) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                float hover = (iconType == 0) ? studentHover : adminHover;

                // Card background — glassmorphic with hover brightness
                int bgAlpha = (int) (12 + hover * 10);
                g2.setColor(new Color(255, 255, 255, bgAlpha));
                g2.fillRoundRect(0, 0, w, h, 24, 24);

                // Border with accent tint on hover
                int borderAlpha = (int) (20 + hover * 50);
                Color borderCol = new Color(accentColor.getRed(), accentColor.getGreen(),
                        accentColor.getBlue(), borderAlpha);
                g2.setColor(borderCol);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 24, 24);

                // Top glow on hover
                if (hover > 0.01f) {
                    GradientPaint topGlow = new GradientPaint(0, 0,
                            new Color(accentColor.getRed(), accentColor.getGreen(),
                                    accentColor.getBlue(), (int) (hover * 30)),
                            0, h / 3, new Color(accentColor.getRed(), accentColor.getGreen(),
                                    accentColor.getBlue(), 0));
                    g2.setPaint(topGlow);
                    g2.fillRoundRect(0, 0, w, h, 24, 24);
                }

                // === ICON ===
                int iconCX = 40, iconCY = 45, iconR = 22;
                // Icon circle background
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(),
                        accentColor.getBlue(), 40 + (int) (hover * 30)));
                g2.fillOval(iconCX - iconR, iconCY - iconR, iconR * 2, iconR * 2);

                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (iconType == 0) {
                    // Graduation cap icon
                    int cx = iconCX, cy = iconCY;
                    // Cap body (diamond shape)
                    g2.drawLine(cx - 12, cy - 2, cx, cy - 10);
                    g2.drawLine(cx, cy - 10, cx + 12, cy - 2);
                    g2.drawLine(cx + 12, cy - 2, cx, cy + 4);
                    g2.drawLine(cx, cy + 4, cx - 12, cy - 2);
                    // Tassel
                    g2.drawLine(cx + 12, cy - 2, cx + 12, cy + 8);
                    g2.fillOval(cx + 10, cy + 7, 5, 5);
                    // Bottom U shape (mortarboard rim)
                    g2.drawArc(cx - 8, cy, 16, 12, 180, 180);
                } else {
                    // Shield / admin icon
                    int cx = iconCX, cy = iconCY;
                    Path2D shield = new Path2D.Float();
                    shield.moveTo(cx, cy - 13);
                    shield.lineTo(cx + 12, cy - 7);
                    shield.lineTo(cx + 12, cy + 2);
                    shield.quadTo(cx + 10, cy + 12, cx, cy + 14);
                    shield.quadTo(cx - 10, cy + 12, cx - 12, cy + 2);
                    shield.lineTo(cx - 12, cy - 7);
                    shield.closePath();
                    g2.draw(shield);
                    // Checkmark inside
                    g2.drawLine(cx - 5, cy + 1, cx - 1, cy + 5);
                    g2.drawLine(cx - 1, cy + 5, cx + 6, cy - 4);
                }

                // === TEXT ===
                int textX = 30;
                int textY = 85;

                // Title
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g2.setColor(Color.WHITE);
                g2.drawString(title, textX, textY);

                // Description
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.setColor(new Color(148, 163, 184));
                drawWrappedText(g2, desc, textX, textY + 24, w - 60);

                // Features list
                int featY = textY + 70;
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                for (String feat : features) {
                    // Bullet dot
                    g2.setColor(accentColor);
                    g2.fillOval(textX, featY - 4, 6, 6);
                    // Text
                    g2.setColor(new Color(203, 213, 225));
                    g2.drawString(feat, textX + 14, featY);
                    featY += 22;
                }

                // "Enter Portal →" button area
                int btnY = h - 55;
                int btnW = w - 60;
                int btnH = 38;
                int btnX = 30;

                // Button fill
                Color btnColor = new Color(
                        accentColor.getRed() + (int) ((accentDark.getRed() - accentColor.getRed()) * hover),
                        accentColor.getGreen() + (int) ((accentDark.getGreen() - accentColor.getGreen()) * hover),
                        accentColor.getBlue() + (int) ((accentDark.getBlue() - accentColor.getBlue()) * hover),
                        180 + (int) (hover * 75));
                g2.setColor(btnColor);
                g2.fillRoundRect(btnX, btnY, btnW, btnH, 12, 12);

                // Button text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.setColor(Color.WHITE);
                String btnText = "Enter Portal  \u2192";
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(btnText);
                g2.drawString(btnText, btnX + (btnW - tw) / 2, btnY + (btnH + fm.getAscent() - fm.getDescent()) / 2);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(380, 290));
        return card;
    }

    // Utility to draw wrapped text
    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word);
                lineY += fm.getHeight() + 2;
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString(), x, lineY);
        }
    }

    // ======================== FEATURES SECTION ========================
    private JPanel createFeaturesSection() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(40, 80, 20, 80));

        // Section title
        JLabel heading = new JLabel("Why Choose Our Platform?");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(heading);
        wrapper.add(Box.createVerticalStrut(30));

        // Feature cards row
        JPanel row = new JPanel(new GridLayout(1, 3, 24, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(900, 160));

        row.add(createFeatureCard("\uD83D\uDE80", "Real-Time Tracking",
                "Monitor your application status with live updates and instant notifications."));
        row.add(createFeatureCard("\uD83D\uDCC8", "Smart Analytics",
                "Data-driven insights into placement trends, company preferences, and student performance."));
        row.add(createFeatureCard("\uD83D\uDD12", "Secure & Reliable",
                "Enterprise-grade security for your personal data, resumes, and credentials."));

        wrapper.add(row);
        return wrapper;
    }

    private JPanel createFeatureCard(String emoji, String title, String desc) {
        JPanel card = new JPanel() {
            private float hoverProgress = 0f;
            private Timer hTimer;
            {
                hTimer = new Timer(16, null);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hTimer.stop();
                        hTimer = new Timer(16, ev -> {
                            hoverProgress = Math.min(1f, hoverProgress + 0.1f);
                            repaint();
                            if (hoverProgress >= 1f)
                                hTimer.stop();
                        });
                        hTimer.start();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hTimer.stop();
                        hTimer = new Timer(16, ev -> {
                            hoverProgress = Math.max(0f, hoverProgress - 0.1f);
                            repaint();
                            if (hoverProgress <= 0f)
                                hTimer.stop();
                        });
                        hTimer.start();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int bgA = (int) (8 + hoverProgress * 10);
                g2.setColor(new Color(255, 255, 255, bgA));
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                int bA = (int) (15 + hoverProgress * 30);
                g2.setColor(new Color(255, 255, 255, bA));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

                // Emoji
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
                g2.drawString(emoji, 20, 42);

                // Title
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.setColor(Color.WHITE);
                g2.drawString(title, 20, 72);

                // Desc
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(148, 163, 184));
                drawWrappedText(g2, desc, 20, 92, w - 40);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(260, 140));
        return card;
    }

    // ======================== FOOTER ========================
    private JPanel createFooter() {
        JPanel footer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Subtle top border line
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRect(60, 0, getWidth() - 120, 1);
                g2.dispose();
            }
        };
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(25, 60, 30, 60));

        JLabel line1 = new JLabel("Placement Management System  \u2022  Empowering Careers Since 2024");
        line1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        line1.setForeground(new Color(100, 116, 139));
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(line1);

        footer.add(Box.createVerticalStrut(6));

        JLabel line2 = new JLabel("\u00A9 2026 Career Placement Portal. All Rights Reserved.");
        line2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        line2.setForeground(new Color(71, 85, 105));
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(line2);

        return footer;
    }
}