package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Drive;
import util.DBConnection;

public class DriveDAO {

    public boolean createDrive(Drive drive) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "INSERT INTO drive(company_id,drive_date,min_cgpa) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1,
                    drive.getCompanyId());

            ps.setString(2,
                    drive.getDriveDate());

            ps.setDouble(3,
                    drive.getMinCgpa());

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {

            e.printStackTrace();
            return false;
        }
    }
public ResultSet getAllDrives() {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT d.drive_id, c.company_name, c.company_package, d.drive_date, d.min_cgpa " +
                "FROM drive d " +
                "JOIN company c ON d.company_id = c.company_id";

        PreparedStatement ps =
                con.prepareStatement(query);

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}
public double getMinimumCgpa(
        int driveId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT min_cgpa FROM drive WHERE drive_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, driveId);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getDouble(
                    "min_cgpa");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return 0;
}
public ResultSet getEligibleDrives(
        double studentCgpa) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT d.drive_id, " +
        "c.company_name, " +
        "d.drive_date, " +
        "d.min_cgpa " +
        "FROM drive d " +
        "JOIN company c " +
        "ON d.company_id=c.company_id " +
        "WHERE d.min_cgpa <= ?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setDouble(1, studentCgpa);

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}

    public double getMinCgpaForDrive(int driveId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT min_cgpa FROM drive WHERE drive_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, driveId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("min_cgpa");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
