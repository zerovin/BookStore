package com.sist.client;
import javax.swing.*; //javafx
public class Login extends JFrame{

	public JLabel la1,la2;
	public JTextField tf;
	public JPasswordField pf;
	public JButton b1,b2;
	
	public Login() {
		setTitle("로그인");
		//초기화
		la1=new JLabel("아이디");
		la2=new JLabel("비밀번호");
		tf=new JTextField();
		pf=new JPasswordField();
		b1=new JButton("로그인");
		b2=new JButton("취소");
		
		//배치
		//사용자 정의
		setLayout(null);
		la1.setBounds(10, 15, 80, 30);
		tf.setBounds(100, 15, 200, 30);
		la2.setBounds(10, 50, 80, 30);
		pf.setBounds(100, 50, 200, 30);
		
		JPanel p=new JPanel();
		p.add(b1);
		p.add(b2);
		p.setBounds(10, 90, 290, 35);
		
		//윈도우에 추가
		add(la1);
		add(tf);
		add(la2);
		add(pf);
		add(p);
		
		//윈도우 크기 결정
		setSize(350, 170);
		
		//출력여부
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//생성자호출
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		}catch(Exception ex) {}
		
		new Login();
	}

}
