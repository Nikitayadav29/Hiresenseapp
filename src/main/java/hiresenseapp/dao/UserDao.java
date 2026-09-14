package hiresenseapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hiresenseapp.dbutils.DBConnection;
import hiresenseapp.pojo.UserPojo;

public class UserDao {
public static int registerUser(UserPojo user)throws Exception{
	Connection conn=null;
	PreparedStatement ps=null;
	int count=0;
	try {
		conn=DBConnection.getConnection();
		ps=conn.prepareStatement("Insert into users(name,email,password,role)values(?,?,?,?)");
		ps.setString(1,user.getName());
		ps.setString(2,user.getEmail());
		ps.setString(3,user.getPassword());
		ps.setString(4,user.getRole());
		count=ps.executeUpdate();
	}finally {
		if(ps!=null) {
			ps.close();
		}
		return count;
	}
}
	public static UserPojo getUserByEmail(String email)throws Exception{
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		UserPojo user=null;
		try {
			conn=DBConnection.getConnection();
			ps=conn.prepareStatement("Select*from users where email=?");
			ps.setString(1, email);
			rs=ps.executeQuery();
			if(rs.next()) {
			    user=new UserPojo();
				user.setId(rs.getInt("Id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				user.setStatus(rs.getString("status"));
				user.setCreatedAt(rs.getDate("created_at"));
			}	
		}finally {
				if(rs!=null) {
					rs.close();
				}
				if(ps!=null) {
					ps.close();
				}
				return user;
			}
		}
		public static UserPojo getUserById(int id)throws Exception{
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			UserPojo user=null;
			try {
				conn=DBConnection.getConnection();
				ps=conn.prepareStatement("Select*from users where id=?");
				ps.setInt(1, id);
				rs=ps.executeQuery();
				if(rs.next()) {
				    user=new UserPojo();
					user.setId(rs.getInt("Id"));
					user.setName(rs.getString("name"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setRole(rs.getString("role"));
					user.setStatus(rs.getString("status"));
					user.setCreatedAt(rs.getDate("created_at"));	
			}
				
			}finally {
				if(rs!=null) {
					rs.close();
				}
				if(ps!=null) {
					ps.close();
				}
				return user;
			}
				
	}
		public static List<UserPojo> getAllUsers()throws Exception{                                  
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
		List<UserPojo>userList=new ArrayList<>();
		try {
			conn=DBConnection.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery("Select*from users order by id desc");
			while(rs.next()) {
				UserPojo user=new UserPojo();
				user.setId(rs.getInt("Id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				user.setStatus(rs.getString("status"));
				user.setCreatedAt(rs.getDate("created_at"));
				
				
			}
		}finally {
			if(rs!=null) {
				rs.close();
			}
			if(st!=null) {
				st.close();
			}
			return userList;
		}
	}
	
public static int updateStatus(int userId,String status)throws Exception{
	Connection conn=null;
	PreparedStatement ps=null;
	int count=0;
	try {
		conn=DBConnection.getConnection();
		ps=conn.prepareStatement("Update users set status=? where id=?");
		ps.setString(1, status);
		ps.setInt(2, userId);
		count=ps.executeUpdate();
	}finally {
		if(ps!=null) {
			ps.close();
		}
		return count;
	}
}
public static List<UserPojo> getFilteredUsers(String search, String role, String status) {
    List<UserPojo> users = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = DBConnection.getConnection(); // Replace with your connection method

        // Base query
        StringBuilder query = new StringBuilder("SELECT * FROM users WHERE role != 'admin'");

        // Dynamic filters
        if (search != null && !search.trim().isEmpty()) {
            query.append(" AND (name LIKE ? OR email LIKE ?)");
        }
        if (role != null && !role.trim().isEmpty() && !role.equalsIgnoreCase("all")) {
            query.append(" AND role = ?");
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            query.append(" AND status = ?");
        }

        query.append(" ORDER BY created_at DESC");

        ps = conn.prepareStatement(query.toString());

        // Set dynamic parameters
        int index = 1;
        if (search != null && !search.trim().isEmpty()) {
            ps.setString(index++, "%" + search.trim() + "%");
            ps.setString(index++, "%" + search.trim() + "%");
        }
        if (role != null && !role.trim().isEmpty() && !role.equalsIgnoreCase("all")) {
            ps.setString(index++, role);
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            ps.setString(index++, status);
        }

        rs = ps.executeQuery();

        while (rs.next()) {
            UserPojo user = new UserPojo();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setStatus(rs.getString("status"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            users.add(user);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return users;
}



}


		