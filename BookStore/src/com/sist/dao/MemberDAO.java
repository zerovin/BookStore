package com.sist.dao;
import java.util.*;
import java.lang.reflect.Member;
import java.rmi.MarshalException;
import java.sql.*;
public class MemberDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.10.124:1521:XE";
	private static MemberDAO dao; //싱글턴
	
	//1.드라이버 등록
	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	
	//2.오라클 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr2","happy"); //conn hr/happy
		}catch(Exception ex) {}
	}
	
	//3.오라클 해제
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
	
	//4.싱글턴 => 한사람당 한개의 DAO만 사용 => 메모리 누수현상 방지
	public static MemberDAO newInstance() {
		if(dao==null) {
			dao=new MemberDAO();
		}
		return dao; //null이 아니면 기존에 저장된 dao 전송
	}
	//----------------------------------- 모든 DAO에 공통사항
	
	//기능
	//1.로그인처리
	/*
	 *  리턴형 => 경우의 수
	 *  - 사번이 없는 경우 => NOSABUN / 0
	 *  - 사번은 있으나 이름이 틀린 경우 => NONAME / 1
	 *  - 사번은 존재, 이름도 동일 => OK 2
	 *  => String / int
	 */
	public String memberLogin(String id, String pwd) {
		String result="";
		try {
			//1.연결
			getConnection();
			
			//2.SQL 문장
			String sql="SELECT COUNT(*) FROM member " //사번이 존재하는지 확인 COUNT 0 or 1
					+"WHERE id=?";  //'"+id+"'";
			
			//3.오라클로 SQL문장 전송
			ps=conn.prepareStatement(sql);
			
			//?에 값 채우기
			ps.setString(1, id);
			
			//4.결과값을 받는다
			ResultSet rs=ps.executeQuery();
			rs.next(); //값이 0아니면 1이라 커서 한번만 옮기면됨 while문 돌릴 필요X
			int count=rs.getInt(1); // 0 or 1
			rs.close();
			if(count==0) { //ID가 없는 경우
				result="NOID";
			}else { //ID가 있는 경우
				sql="SELECT pwd FROM member "
						+"WHERE id=?";
				//오라클로 전송
				ps=conn.prepareStatement(sql);
				
				//? 값 채우기
				ps.setString(1, id);
				
				//결과값 받기
				rs=ps.executeQuery();
				rs.next();
				String db_pwd=rs.getString(1);
				rs.close();
				
				if(db_pwd.equals(pwd)) { //로그인
					result="OK";
				}else { //이름 불일치
					result="NOPWD";
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace(); //오류확인 => null / sql문장
		}finally {
			//오라클 해제
			disConnection();
		}
		return result;
	}
	//1-1. 회원 정보 읽기
	public MemberVO memberInfo(String id) {
		MemberVO vo=new MemberVO();
		try {
			getConnection();
			String sql="SELECT id, name, sex, admin "
					+"FROM member "
					+"WHERE id=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setId(rs.getString(1));
			vo.setName(rs.getString(2));
			vo.setSex(rs.getString(3));
			vo.setAdmin(rs.getString(4));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	public MemberVO memberInfo2(String id) {
		MemberVO vo=new MemberVO();
		try{
			getConnection();
			String sql="SELECT name, sex, addr1, phone, content, email "
					+"FROM member "
					+"WHERE id=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setName(rs.getString(1));
			vo.setSex(rs.getString(2));
			vo.setAddr1(rs.getString(3));
			vo.setPhone(rs.getString(4));
			vo.setContent(rs.getString(5));
			vo.setEmail(rs.getString(6));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	//2.회원가입 - 아이디중복체크, 우편번호검색
	/*
	  ID               NOT NULL VARCHAR2(20)
      PWD              NOT NULL VARCHAR2(10)
      NAME             NOT NULL VARCHAR2(51)
      SEX                       CHAR(6)
      BIRTHDAY                  VARCHAR2(10)
      POST             NOT NULL VARCHAR2(7)
      ADDR1            NOT NULL VARCHAR2(150)
      ADDR2                     VARCHAR2(150)
      PHONE                     VARCHAR2(13)
      EMAIL                     VARCHAR2(100)
      CONTENT                   CLOB
      REGDATE                   DATE
      ADMIN                     CHAR(1) 
	 */
	public String memberInsert(MemberVO vo) {
		/*
		   Statement 한번에 값 채우기
		   String sql="INSERT INTO member VALUES('"+vo.getID()+"','"+vo.getPwd+"','"......
		 */
		String result="";
		try {
			getConnection();
			String sql="INSERT INTO member VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 'n')";
			ps=conn.prepareStatement(sql);
			
			//물음표에 값 채우기
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPwd());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getSex());
			ps.setString(5, vo.getBirthday());
			ps.setString(6, vo.getPost());
			ps.setString(7, vo.getAddr1());
			ps.setString(8, vo.getAddr2());
			ps.setString(9, vo.getPhone());
			ps.setString(10, vo.getEmail());
			ps.setString(11, vo.getContent());
			
			//추가요청
			ps.executeUpdate(); //INSERT / UPDATE / DELETE에 사용
			//executeQuery() 데이터를 가지고 온다 SELECT 사용
			//COMMIT 포함 => 바로저장
			//executeUpdate()에 conn.commit()포함
			result="yes";
		}catch(Exception ex) {
			result=ex.getMessage();
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return result;
	}
	// 2-1. 아이디중복체크
	public int memberIdCheck(String id) {
		int count=0;
		try {
			getConnection();
			String sql="SELECT COUNT(*) FROM member "
					+"WHERE id=?";
			ps=conn.prepareStatement(sql);
			
			//물음표에 값 채우기
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			rs.next();
			count=rs.getInt(1); //0 or 1
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return count;
	}
	//2-2.우편번호 검색
	public ArrayList<ZipcodeVO> postFindData(String dong){
		ArrayList<ZipcodeVO> list=new ArrayList<ZipcodeVO>();
		try {
			getConnection();
			String sql="SELECT zipcode, sido, gugun, dong, NVL(bunji,' ') "
					+ "FROM zipcode "
					+ "WHERE dong LIKE '%'||?||'%'";
			ps=conn.prepareStatement(sql);
			ps.setString(1, dong); //자동으로 '서교', setString은 자동으로 작은따옴표 붙여줌
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
	//3.회원수정
	//4.회원탈퇴
	//SQL문장 제작할 줄 알아야 웹도 가능 DAO는 변경X
}
