package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

import dao.NoticeDAO;
import model.Notice;
import util.components.AnimatedButton;

public class NoticeBoardForm extends JPanel {
    private NoticeDAO noticeDAO;
    private boolean isAdmin;
    private JPanel boardPanel;
    private int selectedNoticeId = -1;

    public NoticeBoardForm(boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.noticeDAO = new NoticeDAO();
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 240, 230));
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("📌 College Notice Board");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(60, 30, 10));

        JLabel subtitleLabel = new JLabel("Pin notices for students — click a note to select it");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(120, 90, 60));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        // Board area (cork texture feel)
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Draw cork-like dots pattern
                g2.setColor(new Color(190, 150, 100, 40));
                for (int x = 0; x < getWidth(); x += 30) {
                    for (int y = 0; y < getHeight(); y += 30) {
                        g2.fillOval(x, y, 3, 3);
                    }
                }
                g2.dispose();
            }
        };
        boardPanel.setBackground(new Color(210, 175, 125));
        boardPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20));
        boardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(160, 120, 80), 2, true));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(210, 175, 125));
        add(scrollPane, BorderLayout.CENTER);

        loadNotices();

        if (isAdmin) {
            AnimatedButton addBtn = new AnimatedButton("+ Post New Notice", new Color(45, 125, 50));
            AnimatedButton deleteBtn = new AnimatedButton("🗑 Delete Selected", new Color(180, 40, 40));

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            bottomPanel.setOpaque(false);
            bottomPanel.add(addBtn);
            bottomPanel.add(deleteBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> showAddNoticeDialog());
            deleteBtn.addActionListener(e -> deleteSelectedNotice());
        }
    }

    private void loadNotices() {
        boardPanel.removeAll();
        selectedNoticeId = -1;

        // Post-it color palette (warm tones)
        Color[] noteColors = {
            new Color(255, 242, 120), // Yellow
            new Color(180, 240, 180), // Green
            new Color(255, 200, 180), // Salmon
            new Color(200, 220, 255), // Blue
            new Color(255, 220, 150), // Orange
            new Color(230, 200, 255), // Lavender
        };

        List<Notice> notices = noticeDAO.getAllActiveNotices();
        int idx = 0;
        for (Notice notice : notices) {
            Color noteColor = noteColors[idx % noteColors.length];
            boardPanel.add(createStickyNote(notice, noteColor));
            idx++;
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JPanel createStickyNote(Notice notice, Color baseColor) {
        // Slightly darker shade for shadow
        Color shadowColor = new Color(
            Math.max(0, baseColor.getRed() - 30),
            Math.max(0, baseColor.getGreen() - 30),
            Math.max(0, baseColor.getBlue() - 30)
        );

        JPanel note = new JPanel() {
            private boolean isSelected = false;
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            {
                hoverTimer = new Timer(15, e -> {
                    if (isHovered() && hoverProgress < 1f) { hoverProgress = Math.min(1f, hoverProgress + 0.12f); repaint(); }
                    else if (!isHovered() && hoverProgress > 0f) { hoverProgress = Math.max(0f, hoverProgress - 0.12f); repaint(); }
                    else { hoverTimer.stop(); }
                });
            }

            private boolean isHovered() {
                Point mousePos = getMousePosition();
                return mousePos != null;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int lift = (int)(hoverProgress * 6);

                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 40 + (int)(hoverProgress * 20)));
                g2.fillRoundRect(4, 6 + lift, getWidth() - 6, getHeight() - 8, 6, 6);

                // Note face
                g2.setColor(isSelected ? shadowColor : baseColor);
                g2.fillRoundRect(2, 2 - lift, getWidth() - 6, getHeight() - 8, 6, 6);

                // Top fold strip (darker)
                g2.setColor(shadowColor);
                g2.fillRoundRect(2, 2 - lift, getWidth() - 6, 22, 6, 6);
                g2.fillRect(2, 14 - lift, getWidth() - 6, 10);

                // Selection border
                if (isSelected) {
                    g2.setColor(new Color(0, 0, 0, 120));
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawRoundRect(2, 2, getWidth() - 6, getHeight() - 8, 6, 6);
                }

                g2.dispose();
            }

            public void setSelected(boolean sel) {
                this.isSelected = sel;
                repaint();
            }
        };

        note.setLayout(new BorderLayout(0, 6));
        note.setOpaque(false);
        note.setPreferredSize(new Dimension(200, 175));
        note.setBorder(new EmptyBorder(30, 12, 16, 12));
        note.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Pin circle at top
        JLabel pin = new JLabel("📌");
        pin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        pin.setHorizontalAlignment(SwingConstants.CENTER);
        pin.setBorder(new EmptyBorder(-22, 0, 0, 0));

        JLabel titleLbl = new JLabel("<html><b>" + notice.getTitle() + "</b></html>");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(new Color(40, 20, 5));

        JTextArea contentArea = new JTextArea(notice.getContent());
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contentArea.setForeground(new Color(80, 50, 20));
        contentArea.setOpaque(false);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setRows(4);

        JLabel dateLbl = new JLabel(notice.getPostedDate() != null ? notice.getPostedDate().toString() : "");
        dateLbl.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        dateLbl.setForeground(new Color(130, 90, 50));
        dateLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        note.add(pin, BorderLayout.NORTH);
        note.add(titleLbl, BorderLayout.CENTER);
        note.add(new JScrollPane(contentArea) {{
            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(false);
            getViewport().setOpaque(false);
        }}, BorderLayout.CENTER);
        note.add(dateLbl, BorderLayout.SOUTH);

        // Selection click
        final int noticeId = notice.getNoticeId();
        note.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Deselect all other notes
                for (Component c : boardPanel.getComponents()) {
                    if (c instanceof JPanel np && np != note) {
                        try { np.getClass().getMethod("setSelected", boolean.class).invoke(np, false); } catch (Exception ex) {}
                    }
                }
                try { note.getClass().getMethod("setSelected", boolean.class).invoke(note, true); } catch (Exception ex) {}
                selectedNoticeId = noticeId;
            }
        });

        return note;
    }

    private void showAddNoticeDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Post a Notice", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 1, 5, 8));
        form.setOpaque(false);

        JLabel lTitle = new JLabel("Notice Title:");
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField titleField = new JTextField();

        JLabel lContent = new JLabel("Content:");
        lContent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextArea contentArea = new JTextArea(4, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        form.add(lTitle);
        form.add(titleField);
        form.add(lContent);
        dialog.add(form, BorderLayout.NORTH);
        dialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JButton submitBtn = new JButton("📌 Post Notice");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setBackground(new Color(45, 125, 50));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(e -> {
            Notice notice = new Notice();
            notice.setTitle(titleField.getText().trim());
            notice.setContent(contentArea.getText().trim());
            notice.setPostedDate(new Date(System.currentTimeMillis()));
            notice.setActive(true);
            if (noticeDAO.addNotice(notice)) {
                JOptionPane.showMessageDialog(dialog, "Notice posted!");
                loadNotices();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to post notice.");
            }
        });

        dialog.add(submitBtn, BorderLayout.SOUTH);
        dialog.setSize(430, 310);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedNotice() {
        if (selectedNoticeId == -1) {
            JOptionPane.showMessageDialog(this, "Click a sticky note first to select it for deletion.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Remove this notice from the board?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (noticeDAO.deleteNotice(selectedNoticeId)) {
                loadNotices();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete notice.");
            }
        }
    }

    // Helper: WrapLayout — FlowLayout that wraps within container width
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0; rowHeight = 0;
                        }
                        if (rowWidth != 0) rowWidth += hgap;
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                addRow(dim, rowWidth, rowHeight);
                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;
                return dim;
            }
        }
        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);
            if (dim.height > 0) dim.height += getVgap();
            dim.height += rowHeight;
        }
    }
}
