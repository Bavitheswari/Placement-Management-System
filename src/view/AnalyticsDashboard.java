package view;

import javax.swing.*;
import java.awt.GridLayout;

import dao.StatisticsDAO;

public class AnalyticsDashboard extends JFrame {

    public AnalyticsDashboard() {

        setTitle("Placement Analytics");

        setSize(500,400);

        setLayout(
                new GridLayout(
                        7,
                        1,
                        10,
                        10
                )
        );

        StatisticsDAO dao =
                new StatisticsDAO();

        add(new JLabel(
                "Total Students : "
                + dao.totalStudents()
        ));

        add(new JLabel(
                "Total Companies : "
                + dao.totalCompanies()
        ));

        add(new JLabel(
                "Total Drives : "
                + dao.totalDrives()
        ));

        add(new JLabel(
                "Total Applications : "
                + dao.totalApplications()
        ));

        add(new JLabel(
                "Approved : "
                + dao.approvedCount()
        ));

        add(new JLabel(
                "Rejected : "
                + dao.rejectedCount()
        ));

        add(new JLabel(
                "Pending : "
                + dao.pendingCount()
        ));

        setLocationRelativeTo(null);

        setVisible(true);
    }
}