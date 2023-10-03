import java.sql.*;

public class JDBCTest {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			//1 注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2 获取连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode","root","322");
			//3 获取数据库操作对象
			stmt = conn.createStatement();
			//4 执行sql
			String sql = "select empno,ename from emp";
			rs = stmt.executeQuery(sql);
			//5 处理查询结果集
			while(rs.next()){
				String empno = rs.getString("empno");
				String ename = rs.getString("ename");
				System.out.println(empno + "," + ename);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//6关闭资源
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try{
					stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			if (conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
}
class JDBCTest06 {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//1 注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2 获取连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode","root","322");
			//3 获取预编译的数据库操作对象
			String sql = "select ename,sal from emp where empno between ? and ?";
			ps = conn.prepareStatement(sql);
			//4 给 ?(占位符) 传值
			ps.setInt(1,7000);
			ps.setInt(2,8000);
			rs = ps.executeQuery();
			//5 处理查询结果集
			while(rs.next()){
				String ename = rs.getString("ename");
				String sal = rs.getString("sal");
				System.out.println(ename + "," + sal);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//6关闭资源
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			if (conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
}
