package com.sist.client;

import java.awt.Color;

import javax.swing.*;
public class FindPanel extends JPanel{
	public JLabel la3;
	public JTextField tf2;
	public JButton b3;
	
	public FindPanel()
	{
		la3=new JLabel("검색어 입력");
		tf2=new JTextField();
		b3=new JButton("검색");
		
		setLayout(null); // BorderLayout
	    la3.setBounds(250, 150, 100, 30);
	    tf2.setBounds(320, 150, 200, 30);
	    
	    add(la3); add(tf2);
	    
	    JPanel p2 = new JPanel();
	    p2.add(b3);
	    p2.setBounds(450, 150, 200, 50);
	    
	    add(p2);
		
	}

}