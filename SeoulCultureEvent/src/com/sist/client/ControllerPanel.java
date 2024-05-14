package com.sist.client;

import java.awt.CardLayout;
import java.awt.Color;
// 기능별로 나눠서 처리 => 분업 위해서
import javax.swing.*;
public class ControllerPanel extends JPanel{
	public CardLayout card=new CardLayout();
	public HomePanel hp = new HomePanel();
	public FindPanel fp = new FindPanel();
	public ControllerPanel()
	{
		setLayout(card);
		add("HOME",hp);
		add("FIND",fp);
		//setBackground(Color.pink);
	}
	

}
