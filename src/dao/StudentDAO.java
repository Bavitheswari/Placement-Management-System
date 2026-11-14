package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Student;
import util.DBConnection;

public class StudentDAO {

    public boolean registerStudent(Student student) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "INSERT INTO student(name,email,password,department,cgpa,phone) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());
            ps.setString(4, student.getDepartment());
            ps.setDouble(5, student.getCgpa());
            ps.setString(6, student.getPhone());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {

            e.printStackTrace();
            return false;
        }
    }
public boolean loginStudent(String email,
                            String password) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT * FROM student WHERE email=? AND password=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs =
                ps.executeQuery();

        return rs.next();

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
public String getStudentName(String email,
                             String password) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT name FROM student WHERE email=? AND password=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1,email);
        ps.setString(2,password);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getString("name");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return null;
}
public Student getStudentByEmail(String email) {

    try {

        Connection con = DBConnection.getConnection();

        String query =
                "SELECT * FROM student WHERE email=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1, email);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            Student student =
                    new Student();

            student.setStudentId(
                    rs.getInt("student_id"));

            student.setName(
                    rs.getString("name"));

            student.setEmail(
                    rs.getString("email"));

            student.setDepartment(
                    rs.getString("department"));

            student.setCgpa(
                    rs.getDouble("cgpa"));

            student.setPhone(
                    rs.getString("phone"));

            return student;
        }

    } catch(Exception e) {

        e.printStackTrace();
    }
    return null;
}
public int getStudentIdByEmail(
        String email) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT student_id FROM student WHERE email=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1,email);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getInt("student_id");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return -1;
}
public double getStudentCgpa(
        int studentId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT cgpa FROM student " +
        "WHERE student_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, studentId);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getDouble(
                    "cgpa");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return 0;
}
public boolean updateStudent(
        String email,
        String department,
        double cgpa,
        String phone) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "UPDATE student " +
        "SET department=?, cgpa=?, phone=? " +
        "WHERE email=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1, department);
        ps.setDouble(2, cgpa);
        ps.setString(3, phone);
        ps.setString(4, email);

        int rows =
                ps.executeUpdate();

        return rows > 0;

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
public boolean changePassword(
        String email,
        String oldPassword,
        String newPassword) {

    try {

        Connection con =
                DBConnection.getConnection();

        String checkQuery =
        "SELECT * FROM student " +
        "WHERE email=? AND password=?";

        PreparedStatement checkPs =
                con.prepareStatement(checkQuery);

        checkPs.setString(1, email);
        checkPs.setString(2, oldPassword);

        ResultSet rs =
                checkPs.executeQuery();

        if(rs.next()) {

            String updateQuery =
            "UPDATE student " +
            "SET password=? " +
            "WHERE email=?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateQuery);

            updatePs.setString(1, newPassword);
            updatePs.setString(2, email);

            return updatePs.executeUpdate() > 0;
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return false;
}
public boolean updateResume(
        String email,
        String resumePath) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "UPDATE student " +
        "SET resume_path=? " +
        "WHERE email=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1, resumePath);

        ps.setString(2, email);

        return ps.executeUpdate() > 0;

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
public String getResumePathByStudentId(
        int studentId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT resume_path " +
        "FROM student " +
        "WHERE student_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, studentId);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return rs.getString(
                    "resume_path");
        }

    } catch(Exception e) {

        e.printStackTrace();
    }

    return null;
}
public ResultSet searchStudentByName(
        String name) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT * FROM student " +
        "WHERE name LIKE ?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1,
                "%" + name + "%");

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}

    public boolean updateStudentProfile(Student student) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE student SET department=?, cgpa=?, phone=? WHERE email=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, student.getDepartment());
            ps.setDouble(2, student.getCgpa());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getEmail());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateResumePath(int studentId, String path) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE student SET resume_path=? WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, path);
            ps.setInt(2, studentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}