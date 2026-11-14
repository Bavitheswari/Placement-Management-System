package view;

import javax.swing.*;
import java.awt.*;

import dao.StatisticsDAO;

public class StatisticsDashboardForm extends JFrame {

    public StatisticsDashboardForm() {

        setTitle("Placement Statistics Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        StatisticsDAO dao = new StatisticsDAO();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));

        JLabel lblStudents = new JLabel(
                "Total Students : " + dao.totalStudents());

        JLabel lblCompanies = new JLabel(
                "Total Companies : " + dao.totalCompanies());

        JLabel lblDrives = new JLabel(
                "Total Drives : " + dao.totalDrives());

        JLabel lblApplications = new JLabel(
                "Total Applications : " + dao.totalApplications());

        JLabel lblApproved = new JLabel(
                "Approved Applications : " + dao.approvedCount());

        JLabel lblRejected = new JLabel(
                "Rejected Applications : " + dao.rejectedCount());

        JLabel lblPending = new JLabel(
                "Pending Applications : " + dao.pendingCount());

        Font font = new Font("Arial", Font.BOLD, 16);

        lblStudents.setFont(font);
        lblCompanies.setFont(font);
        lblDrives.setFont(font);
        lblApplications.setFont(font);
        lblApproved.setFont(font);
        lblRejected.setFont(font);
        lblPending.setFont(font);

        panel.add(lblStudents);
        panel.add(lblCompanies);
        panel.add(lblDrives);
        panel.add(lblApplications);
        panel.add(lblApproved);
        panel.add(lblRejected);
        panel.add(lblPending);

        add(panel);

        setVisible(true);
    }
}