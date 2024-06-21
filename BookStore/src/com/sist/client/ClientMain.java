package com.sist.client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;

//네트워크 연결
import java.io.*;
import java.net.*;
import java.util.*;
/*
 *  서버에서 전송된 데이터 읽기 => 프로그램을 별도로 동작 => 쓰레드
 *  클라이언트에서 요청하는 프로그램 => 이벤트
 */

import com.sist.commons.Function;
import com.sist.dao.*;
public class ClientMain extends JFrame implements ActionListener, MouseListener, Runnable{
	JLabel logo;
	CardLayout card=new CardLayout();
	LoginPanel loginP=new LoginPanel();
	JoinPanel joinP=new JoinPanel();
	PostFindFrame post=new PostFindFrame(); //우편번호 검색창
	IdCheckFrame idFrm=new IdCheckFrame();
	MenuPanel menuP=new MenuPanel();
	ControllPanel ctrP;
	//네트워크에 필요한 객체
	Socket s; //통신기기 => 핸드폰
	OutputStream out; //서버로 전송
	BufferedReader in; //서버로부터 값 받기
	/*
	 *  1.클라이언트 : 서버정보 ip, port를 알아야 연결가능
	 *              클라이언트 ip는 알 수 있지만 port는 자동생성, port 유동적
	 *  2.서버 : 클라이언트 정보를 알고 있어야 한다
	 *          서버의 ip, port는 개발자가 직접 결정, 상수, 고정적
	 */
	
	//개인마다 필요한 변수
	String myId;
	int selRow=-1;
	//BoardListPanel boardLP=new BoardListPanel();
	public ClientMain() {
		ctrP=new ControllPanel(this);
		setLayout(null);
		
		ImageIcon logo_img=new ImageIcon("img/bookstore_logo.png");
		logo=new JLabel(logo_img);
		logo.setBounds(40, 15, 220, 55);
		add(logo);

		menuP.setBounds(280, 25, 650, 35);
		add(menuP);
		ctrP.setBounds(10, 70, 930, 680);
		add(ctrP);
		
		setSize(960, 780);
		setResizable(false);
		//setVisible(true);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //윈도우 창 우측상단 X버튼으로 종료금지
	
		menuP.homeBtn.addActionListener(this); //홈
		menuP.findBtn.addActionListener(this); //검색
		menuP.newsBtn.addActionListener(this); //뉴스
		menuP.boardBtn.addActionListener(this); //게시판
		menuP.chatBtn.addActionListener(this); //채팅
		menuP.mypageBtn.addActionListener(this); //마이페이지
		menuP.exitBtn.addActionListener(this); //종료
		
		loginP.loginBtn.addActionListener(this); //로그인
		loginP.joinBtn.addActionListener(this); //회원가입
		loginP.cancelBtn.addActionListener(this); //종료
		
		joinP.cancel.addActionListener(this);
		joinP.postFind.addActionListener(this); //우편번호검색
		joinP.idCheck.addActionListener(this); //아이디중복체크
		joinP.join.addActionListener(this); //가입
		
		idFrm.idFind.addActionListener(this); //아이디체크
		idFrm.ok.addActionListener(this); //확인
		
		post.find.addActionListener(this); //우편번호 검색
		post.cancel.addActionListener(this); //우편번호 취소
		post.postTf.addActionListener(this); //우편번호 입력
		post.table.addMouseListener(this); //우편번호 더블클릭
		
		ctrP.chatP.chatInput.addActionListener(this); // 채팅창입력
		ctrP.chatP.table.addMouseListener(this); //채팅대기테이블 클릭
		ctrP.chatP.oneBtn.addActionListener(this); //상담버튼
		ctrP.chatP.infoBtn.addActionListener(this); //정보보기
		ctrP.chatP.sendTf.addActionListener(this); //상담
		ctrP.chatP.oneExit.addActionListener(this); //상담종료
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		}catch(Exception ex) {}
		new ClientMain();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==loginP.cancelBtn) {
			dispose(); //window 메모리 해제
			System.exit(0); //프로그램종료
		}else if(e.getSource()==loginP.loginBtn) {
			//1.입력한 사번, 이름 가지고 오기
			try {
				//유효성 검사
				String id=loginP.idField.getText();
				if(id.length()<1) {
					JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
					loginP.idField.requestFocus();
					return;
				}
				String pwd=String.valueOf(loginP.pwField.getPassword()); //비밀번호 값 가지고 오기 char배열이라 String으로 변환
				if(pwd.length()<1) {
					JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
					loginP.pwField.requestFocus();
					return;
				}
				
				//오라클 연결
				MemberDAO dao=MemberDAO.newInstance();
				String result=dao.memberLogin(id, pwd);
				//웹에서는 자바스크립트 처리
				if(result.equals("NOID")) {
					//없는 아이디
					JOptionPane.showMessageDialog(this, "아이디가 존재하지 않습니다");
					loginP.idField.setText("");
					loginP.pwField.setText("");
					loginP.idField.requestFocus();
				}else if(result.equals("NOPWD")) {
					//비밀번호 불일치
					JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다");
					loginP.pwField.setText("");
					loginP.pwField.requestFocus();
				}else {
					//로그인
					try {
						//1.소켓 생성 => 전화걸기
						s=new Socket("localhost", 7777);
						out=s.getOutputStream();
						in=new BufferedReader(new InputStreamReader(s.getInputStream()));
						out.write((Function.LOGIN+"|"+id+"\n").getBytes());
					}catch(Exception ex) {}
					//서버로 들어오는 값을 받아서 출력
					new Thread(this).start(); //run메소드 호출
				}
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "잘못된 입력입니다");
				return; //메소드 종료
			}
		}else if(e.getSource()==loginP.joinBtn) {
			loginP.setVisible(false);
			joinP.setVisible(true);
			joinP.idF.setText("");
			joinP.pwF.setText("");
			joinP.pw2F.setText("");
			joinP.nameF.setText("");
			joinP.genderM.setSelected(false);
			joinP.genderW.setSelected(false);
			joinP.birthF.setText("");
			joinP.postF.setText("");
			joinP.addr1F.setText("");
			joinP.addr2F.setText("");
			joinP.phoneF1.setText("");
			joinP.phoneF2.setText("");
			joinP.phoneF3.setText("");
			joinP.emailF.setText("");
			joinP.conT.setText("");
		}else if(e.getSource()==joinP.cancel) {
			loginP.setVisible(true);
			joinP.setVisible(false);
		}else if(e.getSource()==menuP.homeBtn){
			ctrP.card.show(ctrP,"HOME");
		}else if(e.getSource()==menuP.findBtn){
			ctrP.card.show(ctrP,"FIND");
		}else if(e.getSource()==menuP.newsBtn) {
			ctrP.card.show(ctrP, "NEWS");
		}else if(e.getSource()==menuP.boardBtn) {
			ctrP.card.show(ctrP, "BOARD");
		}else if(e.getSource()==menuP.chatBtn) {
			ctrP.card.show(ctrP, "CHAT");
		}else if(e.getSource()==menuP.mypageBtn) {
			ctrP.myP.print();
			ctrP.card.show(ctrP, "MYPAGE");
		}else if(e.getSource()==menuP.exitBtn) {
			try {
				out.write((Function.EXIT+"|\n").getBytes());
			}catch(Exception ex) {}
		}else if(e.getSource()==joinP.idCheck) {//-------아이디중복체크
			idFrm.idTf.setText(""); //메모리 초기화
			idFrm.ok.setVisible(false);
			idFrm.alert.setText("");
			idFrm.setVisible(true);
		}else if(e.getSource()==idFrm.idFind) {
			String id=idFrm.idTf.getText();
			if(id.length()<1) {
				JOptionPane.showMessageDialog(this, "아이디를 입력하세요"); // alert()
				idFrm.idFind.requestFocus(); //idTf.focus()
				return;
			}
			//아이디 입력된 상태
			//오라클 연결
			MemberDAO dao=MemberDAO.newInstance();
			int count=dao.memberIdCheck(id);
			if(count==0) {
				idFrm.alert.setText(id+"는(은) 사용 가능한 아이디입니다");
				idFrm.ok.setVisible(true);
			}else {
				idFrm.alert.setText(id+"는(은) 이미 사용중인 아이디입니다");
				idFrm.ok.setVisible(false);
				idFrm.idTf.setText("");
				idFrm.idTf.requestFocus();
			}
		}else if(e.getSource()==idFrm.ok) {
			String id=idFrm.idTf.getText();
			joinP.idF.setText(id);
			idFrm.setVisible(false);
		}else if(e.getSource()==joinP.postFind) {//--------우편번호검색
			for(int i=post.model.getRowCount()-1;i>=0;i--) {
				post.model.removeRow(i); //열릴때마다 리스트 리셋, 지우기
			}
			post.postTf.setText("");
			post.setVisible(true);
		}else if(e.getSource()==post.cancel) {
			post.setVisible(false);
		}else if(e.getSource()==post.find || e.getSource()==post.postTf) {
			String dong=post.postTf.getText();
			if(dong.length()<1) {
				JOptionPane.showMessageDialog(this, "동/읍/면을 입력하세요");
				post.postTf.requestFocus();
				return;
			}
			MemberDAO dao=MemberDAO.newInstance();
			ArrayList<ZipcodeVO> list=dao.postFindData(dong);
			if(list.size()==0) {
				JOptionPane.showMessageDialog(this, "검색된 결과가 없습니다");
				post.postTf.setText("");
				post.postTf.requestFocus();
			}else {
				for(int i=post.model.getRowCount()-1;i>=0;i--) {
					post.model.removeRow(i); //열릴때마다 리스트 리셋, 지우기
				}
				for(ZipcodeVO vo:list) {
					String[] data= {vo.getZipcode(), vo.getAddress()};
					post.model.addRow(data);
				}
			}
		}else if(e.getSource()==joinP.join) {
			String id=joinP.idF.getText();
			if(id.length()<1) {
				joinP.idF.requestFocus();
				return;
			}
			String pwd=String.valueOf(joinP.pwF.getPassword());
			if(pwd.length()<1) {
				joinP.pwF.requestFocus();
				return;
			}
			String pwd2=String.valueOf(joinP.pw2F.getPassword());
			if(pwd.length()<1) {
				joinP.pw2F.requestFocus();
				return;
			}
			if(!pwd.equals(pwd2)) {
				joinP.pw2F.requestFocus();
				return;
			}
			String name=joinP.nameF.getText();
			if(name.length()<1) {
				joinP.nameF.requestFocus();
				return;
			}
			String sex="";
			if(joinP.genderM.isSelected()) {
				sex="남자";
			}else {
				sex="여자";
			}
			String birthday=joinP.birthF.getText();
			if(birthday.length()<1) {
				joinP.birthF.requestFocus();
				return;
			}
			String post=joinP.postF.getText();
			if(post.length()<1) {
				joinP.birthF.requestFocus();
				return;
			}
			String addr1=joinP.addr1F.getText();
			if(addr1.length()<1) {
				joinP.addr1F.requestFocus();
				return;
			}
			String phone1=joinP.phoneF1.getText();
			String phone2=joinP.phoneF2.getText();
			String phone3=joinP.phoneF3.getText();
			if(phone1.length()<1) {
				joinP.phoneF1.requestFocus();
				return;
			}
			String phone=phone1+"-"+phone2+"-"+phone3;
			
			String emailId=joinP.emailF.getText();
			String emailSite=joinP.emailCombo.getSelectedItem().toString();
			if(emailId.length()<1) {
				joinP.emailF.requestFocus();
				return;
			}
			String email=emailId+"@"+emailSite;
			
			String addr2=joinP.addr2F.getText();
			String content=joinP.conT.getText();
			MemberVO vo=new MemberVO();
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setSex(sex);
			vo.setBirthday(birthday);
			vo.setPost(post);
			vo.setAddr1(addr1);
			vo.setAddr2(addr2);
			vo.setEmail(email);
			vo.setPhone(phone);
			vo.setContent(content);
			
			MemberDAO dao=MemberDAO.newInstance();
			String res=dao.memberInsert(vo);
			
			if(res.equals("yes")) {
				JOptionPane.showMessageDialog(this, "회원 가입 축하합니다");
				loginP.setVisible(true);
				joinP.setVisible(false);
			}else {				
				JOptionPane.showMessageDialog(this, "회원 가입에 실패하셨습니다\n"+res);
			}
		}else if(e.getSource()==ctrP.chatP.chatInput) {
			String msg=ctrP.chatP.chatInput.getText();
			if(msg.length()<1) {
				return;
			}
			String color=ctrP.chatP.colorBox.getSelectedItem().toString();
			try {
				out.write((Function.CHAT+"|"+msg+"|"+color+"\n").getBytes());
			}catch(Exception ex) {}
			ctrP.chatP.chatInput.setText("");
			ctrP.chatP.chatInput.requestFocus();
		}else if(e.getSource()==ctrP.chatP.infoBtn) {
			if(selRow==-1) {
				JOptionPane.showMessageDialog(this, "정보를 볼 회원을 선택하세요");
				return;
			}
			String yid=ctrP.chatP.model.getValueAt(selRow, 0).toString();
			try {
				out.write((Function.INFO+"|"+yid+"\n").getBytes());
			}catch(Exception ex) {}
		}else if(e.getSource()==ctrP.chatP.oneBtn) {
			String you=ctrP.chatP.userBox.getSelectedItem().toString();
			if(!you.equals("상담자")) {
				try {
					out.write((Function.ONEINIT+"|"+you+"|"+myId+"\n").getBytes());
				}catch(Exception ex) {}
			}else {
				JOptionPane.showMessageDialog(this, "상담자를 선택하세요!!");
			}
		}else if(e.getSource()==ctrP.chatP.sendTf) {
			String youId=ctrP.chatP.youTf.getText();
			String message=ctrP.chatP.sendTf.getText();
			if(message.length()<1) {
				return;
			}
			try {
				out.write((Function.ONETOONE+"|"+youId+"|"+message+"\n").getBytes());
			}catch(Exception ex) {}
			ctrP.chatP.sendTf.setText("");
			ctrP.chatP.sendTf.requestFocus();
		}else if(e.getSource()==ctrP.chatP.oneExit) {
			try{
				String youId=ctrP.chatP.youTf.getText();
				out.write((Function.ONEEXIT+"|"+youId+"\n").getBytes());
			}catch(Exception ex) {}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==post.table) {
			if(e.getClickCount()==2) {
				int row=post.table.getSelectedRow();
				String zip=post.model.getValueAt(row, 0).toString();
				String addr=post.model.getValueAt(row, 1).toString();
				
				joinP.postF.setText(zip);
				joinP.addr1F.setText(addr);
				
				post.setVisible(false);
			}
		}else if (e.getSource() == ctrP.chatP.table) {
	        if (e.getClickCount() == 2) {
	            int row = ctrP.chatP.table.getSelectedRow();
	            selRow = row;
				String id=ctrP.chatP.model.getValueAt(row, 0).toString();
				if(id.equals(myId))
				{
					ctrP.chatP.infoBtn.setEnabled(false);
					ctrP.chatP.oneBtn.setEnabled(false);
				}
				else
				{
					ctrP.chatP.infoBtn.setEnabled(true);
					ctrP.chatP.oneBtn.setEnabled(true);
				}
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				String msg=in.readLine(); //서버 응답값
				StringTokenizer st=new StringTokenizer(msg, "|");
				int delimit=Integer.parseInt(st.nextToken());
				switch(delimit) {
					case Function.LOGIN:{
						String[] data={
								st.nextToken(),
								st.nextToken(),
								st.nextToken()
						};
						ctrP.chatP.model.addRow(data);
						String admin=st.nextToken();
						if(!myId.equals(data[0]) && admin.equals("y")) {
							ctrP.chatP.userBox.addItem(data[0]);
						}
					}
					break;
					case Function.MYLOG:{
						myId=st.nextToken();
						String name=st.nextToken();
						String admin=st.nextToken();
						if(admin.equals("y")) {
							ctrP.chatP.oneBtn.setEnabled(false);
						}
						setTitle(name+"님의 Bookstore");
						loginP.setVisible(false);
						setVisible(true);
					}
					break;
					case Function.CHAT:{
						String message=st.nextToken();
						String color=st.nextToken();
						ctrP.chatP.initStyle();
						ctrP.chatP.append(message, color);
						ctrP.chatP.chatBar.setValue(ctrP.chatP.chatBar.getMaximum());
					}
					break;
					case Function.ONEINIT:{
						String userId=st.nextToken();
						int sel=JOptionPane.showConfirmDialog(this, userId+"님이 상담을 요청하셨습니다", "상담요청", JOptionPane.YES_NO_OPTION);
						if(sel==JOptionPane.YES_OPTION) {
							out.write((Function.ONEYES+"|"+userId+"\n").getBytes());
						}else {
							out.write((Function.ONENO+"|"+userId+"\n").getBytes());
						}
					}
					break;
					case Function.ONENO:{
						String adminId=st.nextToken();
						JOptionPane.showMessageDialog(this, adminId+"님이 거절하셨습니다");
					}
					break;
					case Function.ONEYES:{
						String id=st.nextToken();
						ctrP.chatP.youTf.setText(id);
						ctrP.chatP.pan.setVisible(true);
					}
					break;
					case Function.ONETOONE:{
						ctrP.chatP.ta.append(st.nextToken()+"\n");
						ctrP.chatP.oneBar.setValue(ctrP.chatP.oneBar.getMaximum());
					}
					break;
					case Function.ONEEXIT:
					  {
						  JOptionPane.showMessageDialog(this, "상담을 종료합니다");
						  ctrP.chatP.pan.setVisible(false);
					  }
					  break;
					case Function.INFO:{
						String info="이름 : "+st.nextToken()+"\n"
								+"성별 : "+st.nextToken()+"\n"
								+"주소 : "+st.nextToken()+"\n"
								+"이메일 : "+st.nextToken()+"\n"
								+"전화 : "+st.nextToken()+"\n"
								+"소개 : "+st.nextToken();
						JOptionPane.showMessageDialog(this, info);
						selRow=-1;
					}
					break;
					case Function.MYEXIT:{
						System.exit(0); //윈도우창 종료
					}
					break;
					case Function.EXIT:{
						String yid=st.nextToken();
						for(int i=0;i<ctrP.chatP.model.getRowCount();i++) {
							String s=ctrP.chatP.model.getValueAt(i, 0).toString(); //테이블에 등록된 아이디 읽기
							if(s.equals(yid)) {
								ctrP.chatP.model.removeRow(i);
								break;
							}
						}
						for(int i=0;i<ctrP.chatP.userBox.getItemCount();i++) {
							String s=ctrP.chatP.userBox.getItemAt(i);
							if(s.equals(yid)) {
								ctrP.chatP.userBox.removeItemAt(i);
								break;
							}
						}
					}
					break;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
