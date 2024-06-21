package com.sist.dao;
import java.util.*;
import java.util.Date;
import java.sql.*;
import com.sist.dao.*;


public class WikiDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@192.168.10.124:1521:XE";
	private static WikiDAO dao; // 싱글턴
	
	//1. 드라이버 등록
	public WikiDAO()
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {ex.printStackTrace();}
	}
	
	//2. 오라클 연결
	public void getConnection()
	{
		try 
		{
			conn=DriverManager.getConnection(URL, "hr2", "happy");
			// conn hr/happy
		}catch(Exception ex) {ex.printStackTrace();}
	}
	//3. 오라클 해제
	public void disConnection()
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {ex.printStackTrace();}
	}
	// 4. 싱글턴 => 한사람당 한개의 DAO만 사용할 수 있게 만든다 -> 메모리 누수현상을 제거
	public static WikiDAO newInstance()
	{
		if(dao==null)
			dao = new WikiDAO();
		return dao; // null이 아니면 기존에 저장된 dao를 전송
	}
	//////////////////////////////////// 모든 DAO에 공통 사항
	// 기능설정
	// 1. emp, dept 데이터 출력 (웹, 윈도우) => DAO 변경이 없다 => React / Vue/ Ajax
	// 2. DAO / VO는 변경사항이 없다
	// SQL은 검색언어 => SELECT 
	// 1. 오라클을 배우는 목적 => 자바에서 사용이 가능 => 2주 
	/*
	 * 
NUM
ISBN
BOOKNAME
WRITER
TRANSLATOR
PAGE
PRICE
PUBDATE
SERIES
PAPER
IMAGE
DETAIL
	 * 
	 */
	public int wikiTotalPage()
	{
		int total = 0;
		try 
		{
			//1.연결
			getConnection();
			//2. SQL문장
			String sql = "SELECT CEIL(COUNT(*)/12.0) FROM wiki";
			// 3. 오라클로 전송
			ps = conn.prepareStatement(sql);
			// 4. SQL 문장 실행 결과를 가지고 온다 => 실행 결과를 저장 (ResultSet)
			ResultSet rs = ps.executeQuery();
			// 5. 커서 위치를 데이터에 출력된 첫번째 위치로 이동
			rs.next();
			total = rs.getInt(1);
			// 6. 메모리를 닫는다
			rs.close();
			
			// 쉬운 프로그램은 모든 개발자가 동일한 코딩하는 프로그램 (표준화) => 패턴이 한개
			// ---------- 라이브러리 (MyBatis) => Spring 
			
		}catch(Exception ex)
		{
			// 에러 확인 => 복구(X)
			ex.printStackTrace();
		}
		finally
		{
			// 닫기
			disConnection();
		}
		return total;
	}
	public ArrayList<WikiVO> wikiListData(int page)
	{
		ArrayList<WikiVO> list = new ArrayList<WikiVO>();
		try 
		{
			getConnection();
			String sql = "SELECT NUM, ISBN, BOOKNAME, WRITER, TRANSLATOR, PAGE, PRICE, PUBDATE, SERIES, IMAGE, DETAIL, rnum "
					+ "FROM(SELECT NUM, ISBN, BOOKNAME, WRITER, TRANSLATOR, PAGE, PRICE, PUBDATE, SERIES, IMAGE, DETAIL, rownum as rnum "
					 + "FROM(SELECT NUM, ISBN, BOOKNAME, WRITER, TRANSLATOR, PAGE, PRICE, PUBDATE, SERIES, IMAGE, DETAIL "
					 + "FROM wiki ORDER BY NUM ASC)) "
					 + "WHERE rnum BETWEEN ? AND ?";
				     			
				int rowSize = 12;
				int start=(rowSize*page)-(rowSize-1);
				int end=rowSize*page;
				ps=conn.prepareStatement(sql);
				   ps.setInt(1, start);
				   ps.setInt(2, end);
				ResultSet rs = ps.executeQuery();
				
			while(rs.next())
			{
				WikiVO vo = new WikiVO();
				vo.setNum(rs.getInt(1));
				vo.setIsbn(rs.getLong(2));
				vo.setBookname(rs.getString(3));
				vo.setWriter(rs.getString(4));
				vo.setTranslator(rs.getString(5));
				vo.setPage(rs.getInt(6));
				vo.setPrice(rs.getInt(7));
				vo.setPubdate(rs.getDate(8));
				vo.setSeries(rs.getString(9));
				vo.setImage(rs.getString(10));
				vo.setDetail(rs.getString(11));
								
				list.add(vo);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return list;
	}
	
	public WikiVO bookDetailData(int NUM)
    {
   	 WikiVO vo=new WikiVO();
   	 try
   	 {
   		 getConnection();
 
   		 String sql="SELECT NUM, ISBN, BOOKNAME, WRITER, NVL(TRANSLATOR, '없음'), PAGE, PRICE, PUBDATE, SERIES, IMAGE "
   		    +"FROM wiki "
   		    +"WHERE NUM=?";
   		 
   		 ps=conn.prepareStatement(sql);
   		 // ?에 값을 채운다 
   		 ps.setInt(1, NUM);
   		 
   		 // 결과값 
   		 ResultSet rs=ps.executeQuery();
   		 rs.next();
   		 // 값을 VO에 저장 
   		vo.setNum(rs.getInt(1));
		vo.setIsbn(rs.getLong(2));
		vo.setBookname(rs.getString(3));
		vo.setWriter(rs.getString(4));
		vo.setTranslator(rs.getString(5));
		vo.setPage(rs.getInt(6));
		vo.setPrice(rs.getInt(7));
		vo.setPubdate(rs.getDate(8));
		vo.setSeries(rs.getString(9));
		vo.setImage(rs.getString(10));
				
   		rs.close();

   		 
   	 }catch(Exception ex)
   	 {
   		 ex.printStackTrace();
   	 }
   	 finally
   	 {
   		 disConnection();
   	 }
   	 return vo;
    }
    // 검색 => LIKE 
 public ArrayList<WikiVO> bookFindData(String name)
 {
	  ArrayList<WikiVO> list = new ArrayList<WikiVO>();
	  try
	  {
		  getConnection();
		  String sql="SELECT num, image, bookname, writer, price, series " +
                  "FROM wiki " +
                  "WHERE bookname LIKE '%' || ? || '%' " +
                  "ORDER BY num ASC";
		  ps=conn.prepareStatement(sql);
		  ps.setString(1, name);
		  
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
             WikiVO vo = new WikiVO();
              vo.setNum(rs.getInt(1));
              vo.setImage(rs.getString(2));
              vo.setBookname(rs.getString(3));
              vo.setWriter(rs.getString(4));
              vo.setPrice(rs.getInt(5));
              vo.setSeries(rs.getString(6));
              list.add(vo);
          }
          rs.close();
		  
	  }catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }finally
	  {
		  disConnection();
	  }
	  return list;
 }
    // 구매 => INSERT , UPDATE , DELETE 
	/*
	 * private int bno, bnum, price, account;
	private String id;
	private Date regdate;
	 */
 public void cartInsert(CartVO vo)
 {
	  try 
	  {
		  getConnection();
		  String sql = "INSERT INTO bookcart(bno, bnum, id, price, account) "
				  	  + "VALUES(bookcart_bno_seq.nextval, ?,?,?,?)";
		  ps=conn.prepareStatement(sql);
		  ps.setInt(1, vo.getBnum());
		  ps.setString(2, vo.getId());
		  ps.setInt(3, vo.getPrice());
		  ps.setInt(4, vo.getAccount());
		  
		 ps.executeUpdate();
		  
	  }catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }
	  finally
	  {
		  disConnection();
	  }
 }
 public void cartCancel(int bno)
 {
	  try
	  {
		  getConnection();
		  String sql = "DELETE FROM bookcart "
				  		+ "WHERE bno="+bno;
		  ps=conn.prepareStatement(sql);
		  ps.executeUpdate();
		  
	  }catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }
	  finally
	  {
		  disConnection();
	  }
 }
 public List<CartVO> cartSelect(String id)
 {
	List<CartVO> list = new ArrayList<CartVO>();  
	try
	{
		getConnection();
		 String sql="SELECT bno, price, account, regdate, "
				   +"(SELECT image FROM wiki WHERE num=bookcart.bnum),"
				   +"(SELECT bookname FROM wiki WHERE num=bookcart.bnum),"
				   +"(SELECT price FROM wiki WHERE num=bookcart.bnum) "
				   +"FROM bookcart "
				   +"WHERE id=?";
		ps=conn.prepareStatement(sql);
		ps.setString(1, id);
		ResultSet rs= ps.executeQuery();
		
		while(rs.next()) {
			CartVO vo=new CartVO();
			vo.setBno(rs.getInt(1));
			vo.setPrice(rs.getInt(2));
			vo.setAccount(rs.getInt(3));
			vo.setRegdate(rs.getDate(4));
			vo.getWvo().setImage(rs.getString(5));
			vo.getWvo().setBookname(rs.getString(6));
			vo.getWvo().setPrice(rs.getInt(7));
			list.add(vo);
		}

		rs.close();
					
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally
	{
		disConnection();
	}
	return list;
 }
	
 public List<CartVO> bestSeller()
 {
	List<CartVO> list = new ArrayList<CartVO>();  
	try
	{
		getConnection();
		String sql =  "SELECT bnum,(SELECT bookname FROM wiki WHERE num = bnum) "
				     + "FROM(SELECT bnum "
					 + "FROM(SELECT SUM(account), bnum FROM bookcart "
					 + "group by bnum ORDER BY SUM(account) DESC))";
		
		ps=conn.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		
		while(rs.next())
		{
			CartVO vo = new CartVO();
			vo.setBnum(rs.getInt(1));
			vo.getWvo().setBookname(rs.getString(2));
			
			list.add(vo);
		}
		rs.close();
					
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally
	{
		disConnection();
	}
	return list;
 }
	
	
	
	
}
