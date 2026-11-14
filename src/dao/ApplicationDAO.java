package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Application;
import util.DBConnection;

public class ApplicationDAO {

    public boolean applyForDrive(
            Application application) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
            "INSERT INTO application(student_id,drive_id) VALUES(?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1,
                    application.getStudentId());

            ps.setInt(2,
                    application.getDriveId());

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {

            e.printStackTrace();
            return false;
        }
    }
public ResultSet getApplicationsByStudentId(
        int studentId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT a.application_id, c.company_name, a.status " +
        "FROM application a " +
        "JOIN drive d ON a.drive_id = d.drive_id " +
        "JOIN company c ON d.company_id = c.company_id " +
        "WHERE a.student_id = ?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, studentId);

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}
public ResultSet getAllApplications() {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT a.application_id, " +
        "s.name, " +
        "c.company_name, " +
        "a.status " +
        "FROM application a " +
        "JOIN student s ON a.student_id=s.student_id " +
        "JOIN drive d ON a.drive_id=d.drive_id " +
        "JOIN company c ON d.company_id=c.company_id";

        PreparedStatement ps =
                con.prepareStatement(query);

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}
public boolean updateApplicationStatus(
        int applicationId,
        String status) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "UPDATE application SET status=? WHERE application_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1,status);

        ps.setInt(2,applicationId);

        return ps.executeUpdate() > 0;

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
public boolean alreadyApplied(
        int studentId,
        int driveId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT * FROM application " +
        "WHERE student_id=? AND drive_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, studentId);
        ps.setInt(2, driveId);

        ResultSet rs =
                ps.executeQuery();

        return rs.next();

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
public int getStudentIdByApplicationId(
        int applicationId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT student_id " +
        "FROM application " +
        "WHERE application_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, applicationId);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getInt(
                    "student_id");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return -1;
}
}