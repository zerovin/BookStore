package com.sist.client;
import com.sist.client.Login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientMain extends JFrame implements ActionListener{

	public MenuPanel mp=new MenuPanel();
	public ControllerPanel cp = new ControllerPanel();
	public ClientMain()
	{
		// 배치
		// North, South, West, East, Center => BorderLayout (JFrame)
		setLayout(null);
		mp.setBounds(150, 15, 600, 30);
		add(mp);
		cp.setBounds(0, 50, 800, 530);
		add(cp);
		//add("North",mp);
		//add("Center",cp);
		// 윈도우 크기
		setSize(1024,768);
		setVisible(true);
		setResizable(false); // false => 최대화버튼 사라지게 해줌, 창 크기 변경 못하게 고정
		setLocation(450, 150);
		setDefaultCloseOperation(EXIT_ON_CLOSE); // x 를 누르면 프로그램 종료시켜줌
		
		mp.b1.addActionListener(this);// b1 버튼을 클릭하면 => actionPerformed를 호출해라
		mp.b2.addActionListener(this);
		mp.b6.addActionListener(this);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			
		}catch(Exception ex) {}
		new ClientMain();
		
	}
	@Override // 버튼처리할 때 사용하는 메소드 // 인터페이스
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(mp.b1==e.getSource())// 메뉴 1번 버튼 눌렀을 때
		{
			cp.card.show(cp, "HOME");
			new Login();
		}
		else if(mp.b2==e.getSource())// 메뉴 2번 버튼 눌렀을 때
		{
			cp.card.show(cp, "FIND");
		}
		else if(mp.b6==e.getSource())// 메뉴 6번 버튼 눌렀을 때
		{
			JOptionPane.showMessageDialog(this, "프로그램을 종료합니다");
			System.exit(0);
		}
	}

}