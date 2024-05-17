package com.sist.client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ClientMain extends JFrame implements ActionListener{
	public MenuPanel mp=new MenuPanel();
	public ControllerPanel cp=new ControllerPanel();
	public ClientMain() {
		setLayout(null);
		mp.setBounds(170, 15, 600, 30);
		add(mp);
		cp.setBounds(0, 60, 800, 535);
		add(cp);
		
		//윈도우크기
		setSize(800, 600);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		
		mp.b1.addActionListener(this);
		mp.b2.addActionListener(this);
		mp.b3.addActionListener(this);
		mp.b4.addActionListener(this);
		mp.b5.addActionListener(this);
		mp.b6.addActionListener(this);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		}catch(Exception ex) {}
		new ClientMain();
		//new Login();
	}
	@Override
	public void actionPerformed(ActionEvent e) { 
		// TODO Auto-generated method stub
		if(mp.b1==e.getSource()) { 
			cp.card.show(cp, "HOME");
		}else if(mp.b2==e.getSource()) { 
			cp.card.show(cp, "FIND");
		}else if(mp.b3==e.getSource()) {
			cp.card.show(cp, "NEWS");
		}else if(mp.b4==e.getSource()) {
			cp.card.show(cp, "REVIEW");
		}else if(mp.b5==e.getSource()) {
			cp.card.show(cp, "CHAT");
		}else if(mp.b6==e.getSource()) {
			JOptionPane.showMessageDialog(this, "프로그램을 종료합니다"); 
			System.exit(0);			
		}
	}

}
