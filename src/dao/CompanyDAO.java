package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Company;
import util.DBConnection;

public class CompanyDAO {

    public boolean addCompany(Company company) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "INSERT INTO company(company_name,location,company_package,role) VALUES(?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1,
                    company.getCompanyName());

            ps.setString(2,
                    company.getLocation());

            ps.setDouble(3,
                    company.getCompanyPackage());

            ps.setString(4,
                    company.getRole());

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch(Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public ResultSet getAllCompanies() {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT * FROM company";

        PreparedStatement ps =
                con.prepareStatement(query);

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}
public ResultSet searchCompanyByName(
        String companyName) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
        "SELECT * FROM company " +
        "WHERE company_name LIKE ?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setString(1,
                "%" + companyName + "%");

        return ps.executeQuery();

    } catch(Exception e) {

        e.printStackTrace();
        return null;
    }
}
public boolean deleteCompany(int companyId) {

    try {

        Connection con =
                DBConnection.getConnection();

        String query =
                "DELETE FROM company WHERE company_id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, companyId);

        return ps.executeUpdate() > 0;

    } catch(Exception e) {

        e.printStackTrace();
        return false;
    }
}
}
