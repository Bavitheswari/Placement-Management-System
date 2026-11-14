package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import model.InterviewRound;
import util.DBConnection;

public class InterviewRoundDAO {

    public boolean addRound(InterviewRound round) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO interview_round(drive_id, round_name, round_date) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, round.getDriveId());
            ps.setString(2, round.getRoundName());
            ps.setDate(3, round.getRoundDate());
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
