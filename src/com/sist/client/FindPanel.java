package com.sist.client;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class FindPanel extends JPanel{
   JTable table;
   DefaultTableModel find;
   public JTextField tf;
   public JButton b1,b2,b3,b4,b5,b6;
   public FindPanel()
   {
	   tf=new JTextField();
	   b1=new JButton("검색");
	   
	   JPanel p=new JPanel();
	   p.setLayout(new GridLayout(1,5,20,20)); 
	   b2=new JButton("전체");
	   b3=new JButton("공연");
	   b4=new JButton("전시");
	   b5=new JButton("축제");
	   b6=new JButton("교육/체험");
	   
	   p.add(b2);p.add(b3);p.add(b4);p.add(b5);p.add(b6);
	   p.setBounds(20, 55, 550, 30);
	   add(p);
	   
	   String[] col={"번호","이름","기간","장소","설명"};
	   String[][] row=new String[0][5];
	   find=new DefaultTableModel(row,col);
	   
   	   table=new JTable(find);
   	   JScrollPane js=new JScrollPane(table);
		
       js.setBounds(20, 95, 750, 380);
	   add(js);
   	   
	   setLayout(null); 
	   tf.setBounds(20, 20, 300, 25);
	   add(tf);
	   
	   JPanel p1=new JPanel();
	   p1.add(b1);
	   p1.setBounds(120, 20, 480, 35);
	   add(p1);
   }
}
