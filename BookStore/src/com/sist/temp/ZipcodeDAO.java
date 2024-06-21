package com.sist.temp;
import java.util.*;
import java.sql.*;
public class ZipcodeDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	private static ZipcodeDAO dao;
	
	// 드라이버 등록
	public ZipcodeDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	
	//싱글턴 패턴
	public static ZipcodeDAO newInstance() {
		if(dao==null) {
			dao=new ZipcodeDAO();
		}
		return dao;
	}
	
	//오라클 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL, "hr", "happy");
			//conn hr/happy => 오라클 명령
		}catch(Exception ex) {}
	}
	
	//오라클 닫기
	public void disConnection() {
		try {
			if(ps!=null) {
				ps.close();
			}
			if(conn!=null) {
				conn.close();
			}
		}catch(Exception ex) {}
	}
	
	//검색
	public ArrayList<ZipcodeVO> zipcodeFind(String dong){
		ArrayList<ZipcodeVO> list=new ArrayList<ZipcodeVO>();
		try {
			getConnection();
			String sql="SELECT zipcode, sido, gugun, dong, NVL(bunji,' ') FROM zipcode WHERE dong LIKE '%"+dong+"%'";
			//NVL(bunji,' ') 값이 null일때 ' '공백처리
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				ZipcodeVO vo=new ZipcodeVO();
				vo.setZipcode(rs.getString(1));
				vo.setSido(rs.getString(2));
				vo.setGugun(rs.getString(3));
				vo.setDong(rs.getString(4));
				vo.setBunji(rs.getString(5));
				list.add(vo);
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return list;
	}
}
