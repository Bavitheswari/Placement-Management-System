package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.CompanyDAO;

import java.awt.BorderLayout;
import java.sql.ResultSet;

public class SearchCompanyForm extends JFrame {

    JTextField txtSearch;

    JButton btnSearch;

    JTable table;

    DefaultTableModel model;

    public SearchCompanyForm() {

        setTitle("Search Company");

        setSize(800,400);

        setLayout(new BorderLayout());

        JPanel panel =
                new JPanel();

        panel.add(
                new JLabel("Company Name")
        );

        txtSearch =
                new JTextField(20);

        panel.add(txtSearch);

        btnSearch =
                new JButton("Search");

        panel.add(btnSearch);

        add(panel,
                BorderLayout.NORTH);

        String[] columns = {

                "Company ID",
                "Company Name"
        };

        model =
                new DefaultTableModel(
                        columns,
                        0
                );

        table =
                new JTable(model);

        add(new JScrollPane(table),
                BorderLayout.CENTER);

        btnSearch.addActionListener(
                e -> searchCompany()
        );

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void searchCompany() {

        try {

            model.setRowCount(0);

            CompanyDAO dao =
                    new CompanyDAO();

            ResultSet rs =
                    dao.searchCompanyByName(
                            txtSearch.getText()
                    );

            while(rs.next()) {

                model.addRow(
                        new Object[] {

                                rs.getInt(
                                        "company_id"),

                                rs.getString(
                                        "company_name")
                        }
                );
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}