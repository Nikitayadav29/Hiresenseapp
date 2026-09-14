package hiresenseapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hiresenseapp.dbutils.DBConnection;
import hiresenseapp.pojo.Applicationpojo;


public class ApplicationDao{
	public static boolean apply(Applicationpojo app)throws Exception {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try {
	        conn = DBConnection.getConnection();
	        String sql ="insert into applications (user_id, job_id, resume_path, score) values (?,?,?,?)";
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, app.getUserId());
	        ps.setInt(2, app.getJobId());
	        ps.setString(3, app.getResumePath());
	        ps.setDouble(4, app.getScore());
	        int ans = ps.executeUpdate();
	        return ans > 0;
	    } finally {
	        if (ps != null)
	            ps.close();
	    }
	}
	public static List<Applicationpojo> getApplicationsByUser(int userId) throws Exception {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    List<Applicationpojo> appList = new ArrayList<>();
	    try {
	        conn = DBConnection.getConnection();
	        ps = conn.prepareStatement("Select * from applications where user_id=?");
	        ps.setInt(1, userId);
	        rs = ps.executeQuery();
	        while(rs.next()) {
	            Applicationpojo app = new Applicationpojo();
	            app.setId(rs.getInt("id"));
	            app.setJobId(rs.getInt("job_id"));
	            app.setUserId(rs.getInt("user_id"));
	            app.setResumePath(rs.getString("resume_path"));
	            app.setScore(rs.getInt("score"));
	            app.setStatus(rs.getString("status"));
	            app.setAppliedAt(rs.getString("applied_at"));
	            appList.add(app);
	        }
	        return appList;
	    }finally {
	        if (rs != null)
	            rs.close();
	        if (ps != null)
	            ps.close();
	    }
	  }
	public static List<Applicationpojo> getApplicationsByJobAndStatus(int jobId, String status) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			List<Applicationpojo> list = new ArrayList<>();

			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM applications WHERE job_id = ? AND status = ? ORDER BY score DESC";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, jobId);
			ps.setString(2, status);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new Applicationpojo(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getInt("job_id"),
						rs.getString("resume_path"),
						rs.getFloat("score"),
						rs.getString("status"),
						rs.getString("applied_at")
						));
			}
			return list;
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}

		}

	}
	public static boolean updateApplicationStatus(int appId, String status) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE applications SET status = ? WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, appId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}
}
	    
	

