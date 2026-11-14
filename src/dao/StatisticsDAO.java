package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;

public class StatisticsDAO {

    public int getCount(String query) {

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                return rs.getInt(1);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public int totalStudents() {

        return getCount(
                "SELECT COUNT(*) FROM student"
        );
    }

    public int totalCompanies() {

        return getCount(
                "SELECT COUNT(*) FROM company"
        );
    }

    public int totalDrives() {

        return getCount(
                "SELECT COUNT(*) FROM drive"
        );
    }

    public int totalApplications() {

        return getCount(
                "SELECT COUNT(*) FROM application"
        );
    }

    public int approvedApplications() {

        return getCount(
                "SELECT COUNT(*) FROM application WHERE status='Approved'"
        );
    }
    public int approvedCount() {

        return getCount(
            "SELECT COUNT(*) FROM application WHERE status='Approved'"
        );
    }
    public int rejectedCount() {

        return getCount(
            "SELECT COUNT(*) FROM application WHERE status='Rejected'"
        );
    }
    public int pendingCount() {

        return getCount(
            "SELECT COUNT(*) FROM application WHERE status='Pending'"
        );
    }
}