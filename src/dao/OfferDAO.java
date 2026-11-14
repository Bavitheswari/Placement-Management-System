package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Offer;
import util.DBConnection;

public class OfferDAO {

    public boolean addOffer(Offer offer) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO offer(student_id, company_id, role, ctc, offer_date, status) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, offer.getStudentId());
            ps.setInt(2, offer.getCompanyId());
            ps.setString(3, offer.getRole());
            ps.setDouble(4, offer.getCtc());
            ps.setDate(5, offer.getOfferDate());
            ps.setString(6, offer.getStatus());
            
            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getOffersByStudent(int studentId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT o.offer_id, c.company_name, o.role, o.ctc, o.offer_date, o.status " +
                           "FROM offer o " +
                           "JOIN company c ON o.company_id = c.company_id " +
                           "WHERE o.student_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, studentId);
            return ps.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getAllOffers() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT o.offer_id, s.name, c.company_name, o.role, o.ctc, o.status " +
                           "FROM offer o " +
                           "JOIN student s ON o.student_id = s.student_id " +
                           "JOIN company c ON o.company_id = c.company_id";
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateOfferStatus(int offerId, String status) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE offer SET status=? WHERE offer_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, offerId);
            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
