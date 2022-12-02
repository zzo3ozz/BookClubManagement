package function;

import struct.member;
import java.sql.*;

public class login {
	public final static int SUCCESS = 1;
	public final static int IDERROR = 0;
	public final static int PWERROR = -1;
	public final static int ERROR = -2;
	
	public static int tryLogin(String id, String pw) {
		Connection con = DBConnect.makeConnection(); 
		PreparedStatement pstmt = null;
		int result;
		
		try {
			String sql = "select m_num, id, pw, m_name, level from Member where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String dbPW = rs.getString(3);

				if(dbPW.equals(pw)) {
					// ID 존재, PW 일치할 경우
					member mem = new member(rs);
					
					rs.close();
					pstmt.close();
					con.close();
					
					session.login_member = mem;
					session.login_member.printInfo();
					 
					result = SUCCESS;
				} else {
					System.out.println("비밀번호가 일치하지 않습니다.");
					result = PWERROR;
				}
				
			} else {
				System.out.print("아이디가 존재하지 않습니다.");
				result = IDERROR;
			}
		} catch(SQLException e) {
			System.out.print("error!");
			result = ERROR;
		}

		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}
}