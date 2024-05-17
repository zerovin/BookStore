package com.sist.client;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ReviewPanel extends JPanel{
	JLabel reviewTitle=new JLabel("후기",JLabel.CENTER);
	JLabel pager=new JLabel("0 page / 0 pages");
	JButton newReview,prev,next; 
	JTable table; 
	DefaultTableModel model; 
	public ReviewPanel() {
		newReview=new JButton("새글");
		prev=new JButton("prev");
		next=new JButton("next");
		
		//table
		String[] col={"번호","제목","이름","작성일","조회수"};
		String[][] row=new String[0][5];
		model=new DefaultTableModel(row, col);
		table=new JTable(model);
		JScrollPane js=new JScrollPane(table);
		
		//배치
		setLayout(null);
		reviewTitle.setFont(new Font("맑은 고딕",Font.BOLD,20));
		reviewTitle.setBounds(0, 15, 800, 20);
		add(reviewTitle); //JFrame에 추가
		
		newReview.setBounds(20, 40, 60, 30);
		add(newReview);
		
		js.setBounds(20, 80, 750, 380);
		add(js);
		
		JPanel p=new JPanel();
		p.add(prev);
		p.add(pager);
		p.add(next);
		p.setBounds(20, 470, 750, 35);
		add(p);
	}
}
