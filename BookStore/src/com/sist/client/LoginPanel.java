package com.sist.client;
import javax.swing.*;
import java.awt.*;
public class LoginPanel extends JFrame{
	Image back;
	JLabel titleLa, idLa, pwdLa;
	JTextField idField;
	JPasswordField pwField;
	JButton loginBtn, joinBtn, cancelBtn;
	
	public LoginPanel() {
		back=Toolkit.getDefaultToolkit().getImage("img/login_back.jpg");
		
		setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
			}			
		});
		
		ImageIcon main_logo=new ImageIcon("img/login_logo.png");
		titleLa=new JLabel(main_logo);
		
		idLa=new JLabel("아이디",JLabel.RIGHT);
		pwdLa=new JLabel("비밀번호",JLabel.RIGHT);
		
		idField=new JTextField();
		pwField=new JPasswordField();
		
		loginBtn=new JButton("로그인");
		joinBtn=new JButton("회원가입");
		cancelBtn=new JButton("종료");
		
		//배치
		setLayout(null); //사용자정의
		
		titleLa.setBounds(330, 230, 290, 34);
		add(titleLa);
		
		
		idLa.setBounds(250, 300, 80, 30);
		idField.setBounds(340, 300, 180, 30);
		add(idLa);
		add(idField);
		
		pwdLa.setBounds(250, 340, 80, 30);
		pwField.setBounds(340, 340, 180, 30);
		add(pwdLa);
		add(pwField);
		
		loginBtn.setBounds(540, 300, 100, 70);
		add(loginBtn);
		
		JPanel p=new JPanel();
		p.add(joinBtn);
		p.add(cancelBtn);
		p.setOpaque(false);
		p.setBounds(10, 380, 930, 30);
		add(p);
		
		setSize(960, 750);
		setResizable(false);
		setVisible(true);
	}
	
}
