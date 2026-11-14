package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;

public class AdminDAO {

    public boolean loginAdmin(String username,
                              String password) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT * FROM admin WHERE username=? AND password=?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch(Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
