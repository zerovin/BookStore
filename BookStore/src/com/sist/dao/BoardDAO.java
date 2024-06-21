package com.sist.dao;
/*
 *  DQL / DML 수행
 *  DDL / DCL / TCL 수행X => 오라클에서 작업
 *  TCL => 오라클 명령어X, 메소드 ex)commit(), rollback()
 *         설정이 없으면 AutoCommit()
 *         
 *  프로그램 - 데이터 관리, CRUD (SELECT / INSERT / UPDATE / DELETE)
 *  1. DQL : 데이터 검색 SELECT
 *     자바 === SQL문장(오라클 문법에 맞게) 전송 ===> 오라클
 *     SELECT 형식 - SELECT * | column1, column2...
 *                  FROM table_name | view_name | (SELECT ~)
 *                  => FROM (SELECT~) - 인라인뷰
 *                     ex)페이징, 인기순위(rownum)
 *                  [
 *                     WHERE 조건문(연산자) => 컬럼(함수) 연산자 값
 *                                          값 - 직접대입, 사용자입력값, (SELECT ~)뷰
 *                     GROUP BY 그룹컬럼(함수) => 지정된 그룹별로 따로 설정, 통계
 *                     HAVING 그룹조건 => 반드시 GROUP BY를 동반
 *                                      그룹조건 - 집합함수 ex)COUNT, AVG, SUM
 *                     ORDER BY 컬럼(함수) (ASC|DESC) => 처리속도 늦음 => INDEX로 대체
 *                  ]
 *     **공부순서 : 형식(화면설계) => 연산자(조건에 맞는 데이터 추출) => 내장함수 => 정렬(최신 데이터 먼저출력 DESC)
 *  2. DML : 데이터 조작언어
 *     INSERT : 데이터 저장
 *              형식 1. 컬럼 전체에 값 저장
 *                     INSERT INTO table_name VALUES(값,값...)
 *                     숫자 : 10, 20
 *                     문자 : ' '
 *                     날짜 : SYSDATE(현재날짜), 'YY/MM/DD'
 *                  2. 원하는 컬럼에만 값 저장
 *                     INSERT INTO table_name(컬럼명, 컬럼명...) VALUES(값,값...)
 *                     => 컬럼에 null값 허용되거나 default가 있는 경우 제외 가능
 *     UPDATE : 데이터 수정
 *              형식 - UPDATE table_name SET
 *                    컬럼명=값, 컬럼명=값...
 *                    [WHERE 조건문]
 *     DELETE : 데이터 삭제
 *              형식 - DELETE COLUMN table_name
 *                    [WHERE 조건문]
 *     **주의점 - 자바에서 넘길 때 문자열 " "
 *                           공백주의
 *                           ' '사용
 *                           ; 사용하면 오류 => ;자동추가
 *                           AutoCommint() => 잘못된 데이터 저장시 복구불가
 *              조인 / 서브쿼리 / NULL값 처리(NVL()) ''=null / ' '=공백
 */
//1. 목록 출력 : 번호 / 제목 / 이름 / 작성일 / 조회수
/*
 *  게시판 흐름
 *    - 목록 - 글쓰기 => 완료 => 목록 이동
 *          - 상세 이동
 *    - 상세 - 수정 => 완료 => 상세 이동
 *          - 삭제 => 완료 => 목록 이동
 *          - 목록 이동
 *    
 *  VO / DAO / Service(Open API) / Config(환경설정)
 *  => 윈도우, 모바일, 웹에서 소스가 운영체제 관계없이 모두 동일
 *                      윈도우에서 개발 리눅스에서 호스팅
 */
import java.util.*;

import javax.swing.JOptionPane;

import java.sql.*;
public class BoardDAO {
	//오라클 연결 객체
	private Connection conn; //Socket
	//오라클 송수신 => Socket, OutputStream, BufferedReader => Network
	private PreparedStatement ps;
	//오라클 주소 = 상수형
	private final String URL="jdbc:oracle:thin:@192.168.10.124:1521:XE"; //XE 데이블이 저장된 데이터베이스(폴더)
	//객체 한번만 생성 => 싱글턴
	private static BoardDAO dao;
	//드라이버 등록 => 오라클 연결 => SQL문장 전송 => 결과값 받기 => 데이터를 모아서 윈도우 전송
	public BoardDAO() {
		//시작과 동시에 수행, 멤버변수 초기화
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	
	//오라클 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL, "hr2", "happy");
		}catch(Exception ex) {}
	}
	/*
	 *  진행순서
	 *  Connection
	 *  PreparedStatement
	 *  ResultSet
	 *  
	 *  해제순서
	 *  ResultSet.close()
	 *  PreparedStatement.close()
	 *  Connection.close()
	 */
	//오라클 해제
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
	//싱글턴
	public static BoardDAO newInstance() {
		if(dao==null) {
			dao=new BoardDAO();
		}
		return dao;
	}
	//=====================================공통기반 => 웹 (클래스 => jar) => 라이브러리화
	
	//기능 => 리턴형(어떤 데이터를 보내줄지), 매개변수(사용사로부터 어떤 값을 받을지)
	//1.목록 출력 => 페이징 - 인라인뷰
	//VO - 한개의 게시물 정보 => 컬렉션, 배열로 묶음 => 컬렉션이 편
	/*
	 *                         Collection
	 *                              |
	 *       -----------------------------------------------
	 *       |                      |                      |
	 *      List                   Set                    Map => interface
	 *       |                      |                      |
	 *   ArrayList               HashSet                HashMap
	 *  - 데이터베이스에          - 웹 실시간 채팅(사용자정보)   - 클래스 관리(스프링)
	 *    데이터 모아서 저장       - 순서 없음                  SQL문장 관리(MyBatis)
	 *  - 순서존재              - 데이터 중복 허용X          - 키, 값 두개를 동시에 저장
	 *  - 데이터 중복허용                                    키 : 중복불가
	 *  - 비동기방식                                        값 : 중복가능
	 *    ORDER BY를 사용하지                              ex) Cookie, Session
	 *    않으면 순서가 불일정
	 */
	public List<BoardVO> boardListData(int page){
		List<BoardVO> list=new ArrayList<BoardVO>(); //상위클래스로 생성, VO여러개 리턴
		try {
			//1. 연결
			getConnection();
			//2. 오라클에 보낼 SQL문장
			String sql="SELECT no, subject, name, regdate, hit, num "
					+"FROM (SELECT no, subject, name, regdate, hit, rownum as num "
					+"FROM (SELECT no, subject, name, regdate, hit "
					+"FROM board ORDER BY no DESC)) "
					+"WHERE num BETWEEN ? AND ?";
			ps=conn.prepareStatement(sql);
			int rowSize=10;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				BoardVO vo=new BoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
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
	
	//2.글쓰기 => 시퀀스 사용법 => 매개변수는 특별한 경우를 제외하고 3개초과시 배열, 클래스객체 이용
	//총페이지
	public int boardTotalPage() {
		int total=0;
		try {
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/10.0) FROM board";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	/*
	 *  memberInsert(String a, String b, String c, String d, String e){}
	 *             =>(MemberVO vo)
	 */
	//SELECT 외에는 오라클 자체처리 => 받아올 결과값이 없다 => void
	public void boardInsert(BoardVO vo) {
		try {
			//1.연결
			getConnection();
			//2.SQL문장
			String sql="INSERT INTO board(no, name, subject, content, pwd) "
					+"VALUES(board_no_seq.nextval, ?, ?, ?, ?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			ps.executeUpdate();
			//3.전송
			/*
			 *  executeQuery() => 결과값 가지고 오기 => SELECT
			 *  executeUpdate() => 결과값 보내기 => commit()포함 => INSERT/UPDATE/DELETE 
			 */
			//4.실행요청
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
	}
	//3.상세 => WHERE / 조회수증가, 데이터 읽기
	public BoardVO boardDetailData(int no) {
		BoardVO vo=new BoardVO();
		try {
			//조회수 증가
			getConnection();
			String sql="UPDATE board SET "
					+"hit=hit+1 "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
			
			//데이터 읽기
			sql="SELECT no, name, subject, content, regdate, hit "
					+"FROM board "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	//4.수정 => 비밀번호 체크, 수정
	//4-1. 기존 데이터 읽어오기
	public BoardVO boardUpdateData(int no) {
		BoardVO vo=new BoardVO();
		try {
			getConnection();
			//데이터 읽기
			String sql="SELECT no, name, subject, content "
					+"FROM board "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	//4-2. 수정적용
	public boolean boardUpdate(BoardVO vo) {
		boolean bCheck=false;
		try {
			getConnection();
			String sql="SELECT pwd FROM board WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getNo());
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			if((vo.getPwd()).equals(db_pwd)) {
				bCheck=true;
				//데이터베이스 수정
				sql="UPDATE board SET "
						+"name=?, subject=?, content=? "
						+"WHERE no=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getSubject());
				ps.setString(3, vo.getContent());
				ps.setInt(4, vo.getNo());
				ps.executeUpdate();
			}else {
				bCheck=false;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return bCheck;
	}
	//5.삭제 => 비밀번호 체크, 삭제
	public boolean boardDelete(int no, String pwd) {
		boolean bCheck=false;
		try {
			getConnection();
			String sql="SELECT pwd FROM board WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			if(pwd.equals(db_pwd)) {
				bCheck=true;
				//삭제
				sql="DELETE FROM board WHERE no=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
			}else {
				bCheck=false;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			disConnection();
		}
		return bCheck;
	}
	//기능 수행을 위해서 SQL문장 여러개 필요
}
