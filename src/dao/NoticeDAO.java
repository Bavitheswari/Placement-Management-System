package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Notice;
import util.DBConnection;

public class NoticeDAO {

    public boolean addNotice(Notice notice) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO notice(title, content, posted_date, is_active) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, notice.getTitle());
            ps.setString(2, notice.getContent());
            ps.setDate(3, notice.getPostedDate());
            ps.setBoolean(4, notice.isActive());
            
            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notice> getAllActiveNotices() {
        List<Notice> notices = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM notice WHERE is_active = true ORDER BY posted_date DESC";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()) {
                Notice notice = new Notice();
                notice.setNoticeId(rs.getInt("notice_id"));
                notice.setTitle(rs.getString("title"));
                notice.setContent(rs.getString("content"));
                notice.setPostedDate(rs.getDate("posted_date"));
                notice.setActive(rs.getBoolean("is_active"));
                notices.add(notice);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return notices;
    }

    public boolean deleteNotice(int noticeId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM notice WHERE notice_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, noticeId);
            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
