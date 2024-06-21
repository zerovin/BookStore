package com.sist.client;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.util.*;
public class ChatPanel extends JPanel{
		JTextPane chatpane;
		JTextField chatInput;
		JComboBox<String> colorBox, userBox;
		JTable table;
		DefaultTableModel model;
		JButton oneBtn, infoBtn;
		JScrollBar chatBar;
		
		//상담
		JLabel la;
		JTextField youTf, sendTf;
		JTextArea ta;
		JButton oneExit;
		JPanel pan=new JPanel();
		JScrollBar oneBar;
		public ChatPanel() {
			setLayout(null);
			chatpane=new JTextPane();
			JScrollPane js1=new JScrollPane(chatpane);
			js1.setBounds(10, 45, 630, 570);
			add(js1);
			chatBar=js1.getVerticalScrollBar();
			chatpane.setEditable(false);
			
			chatInput=new JTextField();
			chatInput.setBounds(10, 620, 525, 30);
			add(chatInput);
			
			colorBox=new JComboBox<String>();
			colorBox.addItem("black");
			colorBox.addItem("cyan");
			colorBox.addItem("yellow");
			colorBox.addItem("blue");
			colorBox.addItem("magenta");
			colorBox.addItem("green");
			colorBox.addItem("pink");
			colorBox.addItem("orange");
			
			colorBox.setBounds(540, 620, 100, 30);
			add(colorBox);
			
			String[] col={"ID", "이름", "성별"};
			String[][] row=new String[0][3];
			model=new DefaultTableModel(row, col) {
				@Override
				public boolean isCellEditable(int row, int column) {
					// TODO Auto-generated method stub
					return false;
				}
			};
			table=new JTable(model);
			JScrollPane tableJs=new JScrollPane(table);
			tableJs.setBounds(650, 45, 250, 270);
			add(tableJs);
			
			userBox=new JComboBox<String>();
			userBox.setBounds(650, 320, 80, 30);
			userBox.addItem("상담자");
			add(userBox);
			
			oneBtn=new JButton("1:1상담");
			oneBtn.setBounds(735, 320, 80, 30);
			add(oneBtn);
			infoBtn=new JButton("정보보기");
			infoBtn.setBounds(820, 320, 80, 30);
			add(infoBtn);
			
			la=new JLabel("1:1 대상");
			youTf=new JTextField(10);
			youTf.setEnabled(false);
			oneExit=new JButton("종료");
			
			ta=new JTextArea();
			JScrollPane js3=new JScrollPane(ta);
			oneBar=js3.getVerticalScrollBar();
			ta.setEditable(false);
			
			sendTf=new JTextField(30);
			
			pan.setLayout(new BorderLayout());
			
			JPanel p=new JPanel();
			p.add(la); p.add(youTf);p.add(oneExit);
			pan.add("North",p);			
			pan.add("Center",js3);
			pan.add("South",sendTf);
			
			pan.setBounds(650, 360, 250, 290);
			add(pan);
			
			pan.setVisible(false);
		}
		
		public void initStyle(){
		   Style green=chatpane.addStyle("green", null);
		   StyleConstants.setForeground(green, Color.green);
		   
		   Style yellow=chatpane.addStyle("yellow", null);
		   StyleConstants.setForeground(yellow, Color.yellow);
		   
		   Style blue=chatpane.addStyle("blue", null);
		   StyleConstants.setForeground(blue, Color.blue);
		   
		   Style pink=chatpane.addStyle("pink", null);
		   StyleConstants.setForeground(pink, Color.pink);
		   
		   Style cyan=chatpane.addStyle("cyan", null);
		   StyleConstants.setForeground(cyan, Color.cyan);
		   
		   Style orange=chatpane.addStyle("orange", null);
		   StyleConstants.setForeground(orange, Color.orange);
		   
		   Style magenta=chatpane.addStyle("magenta", null);
		   StyleConstants.setForeground(magenta, Color.magenta);
	       
		   Style red=chatpane.addStyle("red", null);
		   StyleConstants.setForeground(red, Color.red); //알림
		   
		   Style gray=chatpane.addStyle("gray", null);
		   StyleConstants.setForeground(red, Color.gray); //귓속말
		}
		
		//TextPane의 단점 - 문자열 결합 불가 => setText() => append로 문자열 결합
		public void append(String msg, String color) {
			try {
				Document doc=chatpane.getDocument();
				doc.insertString(doc.getLength(), msg+"\n", chatpane.getStyle(color));				
			}catch(Exception ex) {}
		}

	/*
		JLabel chatTitle;
		JTextArea chatTextArea;
		JTextField chatWrite;
		JComboBox chatColor;
		String[] color= {"Black", "Green","Blue","Cyan","Dark gray","Light gray", "Magenta", "Orange", "Pink"};
		JButton chatLetter, chatInfo;
		JTable chatPeople;
		DefaultTableModel chatPeopleData;
		public ChatPanel() {
		
		//title
		chatTitle=new JLabel("실시간 채팅", JLabel.CENTER);
		chatTitle.setFont(new Font("맑은 고딕",Font.BOLD,25));
		chatTitle.setBounds(10,40,940,40);
		add(chatTitle);
		
		//textarea
		chatTextArea=new JTextArea();
		chatWrite=new JTextField();
		chatColor=new JComboBox(color);
		
		setLayout(null);
		
		chatTextArea.setBounds(30, 110, 600, 500);
		add(chatTextArea);
		
		chatWrite.setBounds(30, 620, 480, 30);
		add(chatWrite);
		
		chatColor.setBounds(520, 620, 110, 30);
		add(chatColor);
		
		//table
		String[] col={"아이디","이름"};
		String[][] row=new String[0][2];
		chatPeopleData=new DefaultTableModel(row, col);
		chatPeople=new JTable(chatPeopleData);
		JScrollPane chatTable=new JScrollPane(chatPeople);
		chatTable.setBounds(650, 110, 230, 500);
		add(chatTable);
		
		//table button
		chatLetter=new JButton("1:1 채팅");
		chatInfo=new JButton("정보보기");
		JPanel chatTableBtn=new JPanel();
		chatTableBtn.add(chatLetter);
		chatTableBtn.add(chatInfo);
		chatTableBtn.setBounds(650, 620, 230, 30);
		add(chatTableBtn);	
	}
	*/
	 
}
