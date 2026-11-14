package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

import dao.StudentDAO;
import util.UIConstants;
import util.components.AnimatedButton;

public class SearchStudentForm extends JPanel {

    private final JTextField txtSearch;
    private final JTable table;
    private final DefaultTableModel model;

    public SearchStudentForm() {
        setName("Search Student");
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(0, 15));

        // Header
        JLabel titleLabel = new JLabel("Search Registered Students");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Find students by name — results update in the styled table below");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        // Search bar row
        JPanel searchPanel = new JPanel(new BorderLayout(12, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by student name...");

        AnimatedButton btnSearch = new AnimatedButton("Search", new Color(99, 102, 241));
        btnSearch.setPreferredSize(new Dimension(130, 44));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout(0, 0));
        northPanel.setOpaque(false);
        northPanel.add(titlePanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // Table model
        String[] columns = {"", "Student ID", "Full Name", "Email Address", "Department", "CGPA"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 0 ? ImageIcon.class : Object.class;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                } else {
                    c.setBackground(new Color(224, 231, 255));
                }
                return c;
            }
        };

        // Style the table
        table.setRowHeight(52);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(241, 245, 249));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(new Color(49, 46, 129));
        table.setIntercellSpacing(new Dimension(0, 1));

        // Styled header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(30, 41, 59));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Avatar renderer in first column
        table.getColumnModel().getColumn(0).setCellRenderer((tbl, value, selected, focused, row, col) -> {
            String name = (String) tbl.getValueAt(row, 2);
            Color[] avatarColors = {
                new Color(99, 102, 241), new Color(16, 185, 129), new Color(245, 158, 11),
                new Color(239, 68, 68), new Color(168, 85, 247), new Color(14, 165, 233)
            };
            Color bg = avatarColors[row % avatarColors.length];
            String initials = name != null && name.length() > 0 ?
                (name.split(" ").length > 1 ?
                    String.valueOf(name.charAt(0)) + name.split(" ")[1].charAt(0) :
                    String.valueOf(name.charAt(0))) : "?";

            return new JPanel() {
                { setOpaque(true); setBackground(selected ? new Color(224, 231, 255) : (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252))); }
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    g2.setColor(bg);
                    g2.fillOval(cx - 18, cy - 18, 36, 36);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(initials.toUpperCase(), cx - fm.stringWidth(initials.toUpperCase()) / 2, cy + fm.getAscent() / 2 - 1);
                    g2.dispose();
                }
            };
        });
        table.getColumnModel().getColumn(0).setPreferredWidth(56);
        table.getColumnModel().getColumn(0).setMaxWidth(56);

        // CGPA colored renderer
        table.getColumnModel().getColumn(5).setCellRenderer((tbl, value, selected, focused, row, col) -> {
            double cgpa = value instanceof Double ? (Double) value : 0;
            Color fg = cgpa >= 8.0 ? new Color(21, 128, 61) : cgpa >= 6.0 ? new Color(161, 98, 7) : new Color(185, 28, 28);
            JLabel lbl = new JLabel(String.format("%.2f", cgpa), SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(fg);
            lbl.setOpaque(true);
            lbl.setBackground(selected ? new Color(224, 231, 255) : (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)));
            return lbl;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> searchStudent());
        txtSearch.addActionListener(e -> searchStudent());
        searchStudent();
    }

    private void searchStudent() {
        model.setRowCount(0);
        try {
            StudentDAO dao = new StudentDAO();
            ResultSet rs = dao.searchStudentByName(txtSearch.getText().trim());
            while (rs.next()) {
                model.addRow(new Object[]{
                    null, // avatar placeholder
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getDouble("cgpa")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}