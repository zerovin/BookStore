package com.sist.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.event.*;
import com.sist.dao.*;
public class BoardDetailPanel extends JPanel implements ActionListener{
	JLabel titleLa,nameLa,noLa,subLa,dayLa,hitLa;
    JLabel name,no,sub,day,hit;
    JTextArea ta;
    JButton b1,b2,b3; // 수정 / 삭제 / 목록 
    // nextJs , MSA , DevOps , CI/CD , Docker , 쿠바네티스 , 잰킨스 
    // JavaScript => TypeScript
    ControllPanel ctrP;
    BoardDAO dao;
    public  BoardDetailPanel(ControllPanel ctrP)
    {
     this.ctrP=ctrP;
     dao=BoardDAO.newInstance();
     
     
   	 titleLa=new JLabel("게시글",JLabel.CENTER);// <table>
     titleLa.setFont(new Font("맑은 고딕",Font.BOLD,25)); //<h3></h3>
     setLayout(null);
     titleLa.setBounds(155, 50, 620, 50);
   	 add(titleLa);
   	 
   	 noLa=new JLabel("번호",JLabel.CENTER);
   	 noLa.setOpaque(true); 
   	 noLa.setBackground(new Color(192, 192, 192));
   	 noLa.setBounds(120, 115 , 80, 30);
   	 no=new JLabel("",JLabel.CENTER);
   	 no.setBounds(205, 115, 120, 30);
   	 add(noLa);add(no);
   	 
   	 dayLa=new JLabel("작성일",JLabel.CENTER);
   	 dayLa.setOpaque(true); 
  	 dayLa.setBackground(new Color(192, 192, 192));
   	 dayLa.setBounds(450, 115 , 80, 30);
   	 day=new JLabel("",JLabel.CENTER);
   	 day.setBounds(500, 115, 200, 30);
   	 add(dayLa);add(day);
   	 
   	 nameLa=new JLabel("이름",JLabel.CENTER);
   	 nameLa.setBounds(120, 150 , 80, 30);
   	 nameLa.setOpaque(true); 
  	 nameLa.setBackground(new Color(192, 192, 192));
   	 name=new JLabel("",JLabel.CENTER);
   	 name.setBounds(205, 150, 120, 30);
   	 add(nameLa);add(name);
   	 
   	 hitLa=new JLabel("조회수",JLabel.CENTER);
   	 hitLa.setBounds(450, 150 , 80, 30);
   	 hitLa.setOpaque(true); 
  	 hitLa.setBackground(new Color(192, 192, 192));
   	 hit=new JLabel("",JLabel.CENTER);
   	 hit.setBounds(495, 150, 200, 30);
   	 add(hitLa);add(hit);
   	 
   	 subLa=new JLabel("제목",JLabel.CENTER);
   	 subLa.setBounds(120, 185 , 80, 30);
   	 subLa.setOpaque(true); 
  	 subLa.setBackground(new Color(192, 192, 192));
   	 sub=new JLabel("");
   	 sub.setBounds(225, 185, 400, 30);
   	 add(subLa);add(sub);
   	 
   	 ta=new JTextArea();
   	 ta.setEditable(false); // 비활성화 
   	 ta.setBounds(120, 220, 680, 365);
     ta.setMargin(new Insets(5, 5, 5, 5));
   	 add(ta);
   	 
   	 JPanel p=new JPanel();
   	 b1=new JButton("수정");
   	 b2=new JButton("삭제");
   	 b3=new JButton("목록");
   	 p.add(b1);p.add(b2);p.add(b3);
   	 p.setBounds(230, 600, 485, 35);
   	 add(p);
   	 
   	 b1.addActionListener(this); // 수정
     b2.addActionListener(this); // 삭제
     b3.addActionListener(this); // 목록
   	 
    }
    public void print(int no)
    {
   	 BoardVO vo=dao.boardDetailData(no);
   	 this.no.setText(String.valueOf(vo.getNo()));
   	 name.setText(vo.getName());
   	 sub.setText(vo.getSubject());
   	 hit.setText(String.valueOf(vo.getHit()));
   	 day.setText(vo.getRegdate().toString());
   	 ta.setText(vo.getContent());
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==b3)
		{
			ctrP.boardLP.print();
			ctrP.card.show(ctrP, "BOARD"); // <input type=button value="목록" oneclick="javascript:history.back()">
		}
		else if(e.getSource()==b2)
		{
			ctrP.boardDLP.pf.setText("");
			ctrP.boardDLP.no=Integer.parseInt(no.getText());// <input type=hidden value="10">
			ctrP.card.show(ctrP, "BOARD_DL");
		}
		else if(e.getSource()==b1)
		{
			BoardVO vo=dao.boardUpdateData(Integer.parseInt(no.getText()));
			ctrP.boardUP.no=vo.getNo();
			ctrP.boardUP.nameTf.setText(vo.getName());
			ctrP.boardUP.subTf.setText(vo.getSubject());
			ctrP.boardUP.ta.setText(vo.getContent());
			ctrP.card.show(ctrP, "BOARD_UP");
		}
	}
}


