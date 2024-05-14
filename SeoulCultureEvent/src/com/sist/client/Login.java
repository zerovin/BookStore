package com.sist.client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class Login extends JFrame{
	// 포함 클래스 
	/*
	 *  모든 멤버변수는 private 가 아니다 => 라이브러리는 public으로 설정
	 */
	public JLabel la1, la2;
	public JTextField tf;
	public JPasswordField pf;
	public JButton b1,b2;
	
	// 초기화 => 화면 배치 => 생성자 => 다른 클래스와 연결하기 위해 => public
	public Login()
	{
			setTitle("login");
			// 초기화
			la1=new JLabel("아이디");
			la2=new JLabel("비밀번호");
			
		    tf=new JTextField();
		    pf=new JPasswordField();
		    
		    b1=new JButton("로그인");
		    b2=new JButton("취소");
			
		    // 배치
		    // 사용자 정의 
		    setLayout(null); // BorderLayout
		    la1.setBounds(10, 15, 80, 30);
		    tf.setBounds(100, 15, 200, 30);
		    
		    // 윈도우에 추가
		    add(la1); add(tf);
		    
		    la2.setBounds(10, 50, 80, 30);
		    pf.setBounds(100, 50, 200, 30);
		    
		    // 윈도우에 추가
		    add(la2); add(pf);
		    
		    // 버튼 
		    JPanel p = new JPanel();
		    p.add(b1); p.add(b2); // 가운데 정렬
		    
		    p.setBounds(10, 90, 290, 35);
		    add(p);
		    
		    // 윈도우 크기 결정
		    setSize(330, 170);
		    setLocation(800, 450);
		    // 출력 여부
		    setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 생성자 호출
//		try
//		{
//			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
//			
//		}catch(Exception ex) {}
		
		//new Login();
	}

}