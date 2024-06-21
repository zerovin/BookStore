package com.sist.server;
import java.util.*;
import java.io.*;
import java.net.*;
import com.sist.commons.*;
import com.sist.dao.*;
import com.sist.server.ChatServer.Client;
public class ChatServer implements Runnable{
	/*
	 *  1.회원 관련 => 채팅
	 *  2.나머지는 Client에서 처리, Database
	 */
	private ServerSocket ss;
	private final int PORT=7777;
	
	//접속한 클라이언트 정보 저장
	private Vector<Client> waitVc=new Vector<Client>();
	
	//데이터베이스 연동
	private MemberDAO dao;
	
	//서버가동 - 시작과 동시에 처리 => 생성자
	public ChatServer() {
		try {
			dao=MemberDAO.newInstance();
			ss=new ServerSocket(PORT);
			//같은 컴퓨터에서 서버는 한번만 실행, 두번이상은 오류
			System.out.println("Server Start!!");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	//접속시 처리
	public void run() {
		try {
			while(true) {
				Socket s=ss.accept(); //Socket = 클라이언트 정보 => ip, port
				Client client=new Client(s); //정보 넘겨줌
				client.start(); //정보에 해당되는 클라이언트와의 통신명령
			}
		}catch(Exception ex) {}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatServer server=new ChatServer();
		new Thread(server).start();
	}
	
	//통신담당 => 클라이언트 1개당 한개씩 연결 => 동시에 프로그램 작동
	//프로그램 안에서 여러개의 프로그램 동시작업 => 쓰레드
	//같은 작업 => 통신 => 모든 클라이언트가 다르게 동작가능 => 웹 / 모바일은 이미 웹서버 존재
	/*
	 *  웹 서버 : 송수신
	 *          상세 요청사항, 응답사항은 사용자에 따라 다르기 때문에 각 필요한 사항을 JSP로 생성, 적용
	 *          ex) server.java => case Function.LOGIN:{}, case Function.CHAT:{}...
	 */
	class Client extends Thread{
		//한명과 통신
		//IP받아서 연결
		Socket s;
		//전송위치
		OutputStream out; //out.write() => 클라이언트에게 값 전송=응답
		
		//수신위치
		BufferedReader in; //in.readLine() => 클라이언트의 요청값을 받는다
		
		//기타 개인정보
		String id, name, sex, admin; //관리자 y / 일반사용자 n
		
		//초기화
		public Client(Socket s) {
			try {
				this.s=s;
				in=new BufferedReader(new InputStreamReader(s.getInputStream()));
				out=s.getOutputStream();
			}catch(Exception ex) {}
		}
		
		//통신
		public void run() {
			try {
				while(true) {
					String msg=in.readLine(); //요청값을 받는다
					
					//구분 => 무슨 요청인지, Function
					//구분자 전송 Function.LOGIN 숫자 or 문자, 숫자가 보안에 유리
					StringTokenizer st=new StringTokenizer(msg, "|");
					int delimit=Integer.parseInt(st.nextToken());
					//Function.LOGIN|id|pwd
					switch(delimit) {
						case Function.LOGIN:{
							//id => 데이터베이스(정보읽기)
							id=st.nextToken();
							MemberVO vo=dao.memberInfo(id);
							name=vo.getName();
							sex=vo.getSex();
							admin=vo.getAdmin();
							
							//1.접속된 모든 회원에게 정보 전송
							messageAll(Function.LOGIN+"|"+id+"|"+name+"|"+sex+"|"+admin);
							
							//2.입장 메세지 전송
							messageAll(Function.CHAT+"|[알림 ▶]"+name+"님이 입장하셨습니다|red");
							//3.현재 접속자에게 이미 접속된 회원의 정보 전송
							
							//저장
							waitVc.add(this);
							
							//Login => Home으로 변경
							messageTo(Function.MYLOG+"|"+id+"|"+name+"|"+admin);
							for(Client client:waitVc) {
								messageTo(Function.LOGIN+"|"+client.id+"|"+client.name+"|"+client.sex+"|"+client.admin);
							}
						}
						break;
						case Function.CHAT:{
							String message=st.nextToken();
							String color=st.nextToken();
							messageAll(Function.CHAT+"|["+name+"]"+message+"|"+color);
						}
						break;
						case Function.ONEINIT:{
							String adminId=st.nextToken();
							String userId=st.nextToken();
							for(Client client:waitVc) {
								if(adminId.equals(client.id)) {
									client.messageTo(Function.ONEINIT+"|"+userId);
								}
							}
						}
						break;
						case Function.ONENO:{
							String userId=st.nextToken();
							for(Client client:waitVc) {
								if(userId.equals(client.id)) {
									client.messageTo(Function.ONENO+"|"+id);
								}
							}
						}
						break;
						case Function.ONEYES:{
							String userId=st.nextToken();
							for(Client client:waitVc) {
								if(userId.equals(client.id)) {
									client.messageTo(Function.ONEYES+"|"+id); //상담받는사람
									messageTo(Function.ONEYES+"|"+userId); //상담하는사람
								}
							}
						}
						break;
						case Function.ONETOONE:{
							String userId=st.nextToken();
							String message=st.nextToken();
							for(Client client:waitVc) {
								if(userId.equals(client.id)) {
									client.messageTo(Function.ONETOONE+"|["+name+"]"+message); //상담받는사람
									messageTo(Function.ONETOONE+"|["+name+"]"+message); //상담하는사람
								}
							}
						}
						break;
						case Function.ONEEXIT:{
							String userId=st.nextToken();
							for(Client client:waitVc) {
								if(userId.equals(client.id)) {
									client.messageTo(Function.ONEEXIT+"|"); //상담받는사람
									messageTo(Function.ONEEXIT+"|"); //상담하는사람
								}
							}
						}
						break;
						case Function.INFO:{
								String yid=st.nextToken();
								MemberVO vo=dao.memberInfo2(yid);
								messageTo(Function.INFO+"|"
								          +vo.getName()+"|"
								          +vo.getSex()+"|"
								          +vo.getAddr1()+"|"
								          +vo.getEmail()+"|"
								          +vo.getPhone()+"|"
								          +vo.getContent());
							}
						break;
						case Function.EXIT:{
							//남아있는 사람 처리
							messageAll(Function.EXIT+"|"+id);
							messageAll(Function.CHAT+"|[알림 ▶]"+name+"님이 퇴장하셨습니다|red");
						
							//나간 사람 처리
							for(Client client:waitVc) {
								if(client.id.equals(id)) {
									//1.vector에서 제거
									waitVc.remove(client);

									//2.종료 메세지 전송
									messageTo(Function.MYEXIT+"|");
									
									//3.in out 종료
									in.close();
									out.close();
									
									break;
								}
							}
						}
					}
				}
			}catch(Exception ex) {}
		}
		
		//전체메세지 - 접속자 전체
		public synchronized void messageAll(String msg) {
			try {
				for(Client client:waitVc) {
					client.messageTo(msg);
				}
			}catch(Exception ex) {}
		}
		
		//접속자 한명
		public synchronized void messageTo(String msg) {
			try {
				out.write((msg+"\n").getBytes());
			}catch(Exception ex) {}
		}
	}
}
