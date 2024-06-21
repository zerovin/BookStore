package com.sist.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

public class BoardUpdatePanel extends JPanel implements ActionListener{
	JLabel titleLa,nameLa,subLa,contLa,pwdLa;
    JTextField nameTf,subTf;
    JPasswordField pwdPf;
    JTextArea ta;
    JButton b1,b2;
    ControllPanel ctrP;
    BoardDAO dao;
    int no=0;
    public BoardUpdatePanel(ControllPanel ctrP)
    {
    	this.ctrP=ctrP;
    	dao=BoardDAO.newInstance(); 
    	
    	titleLa=new JLabel("수정하기",JLabel.CENTER);// <table>
    	titleLa.setFont(new Font("맑은 고딕",Font.BOLD,25)); //<h3></h3>
    	setLayout(null);
    	titleLa.setBounds(155, 50, 620, 50);
    	add(titleLa);
    	
    	nameLa=new JLabel("이름",JLabel.CENTER);
    	nameTf=new JTextField();
    	nameLa.setBounds(120, 135, 80, 30);
    	nameTf.setBounds(205, 135, 150, 30);
    	add(nameLa);add(nameTf);
    	
    	subLa=new JLabel("제목",JLabel.CENTER);
    	subTf=new JTextField();
    	subLa.setBounds(120, 165, 80, 30);
    	subTf.setBounds(205, 170, 400, 30);
    	add(subLa);add(subTf);
    	
    	
    	contLa=new JLabel("내용",JLabel.CENTER);
    	ta=new JTextArea();
    	JScrollPane js=new JScrollPane(ta);
    	contLa.setBounds(120, 205, 80, 30);
    	js.setBounds(205, 205, 600, 330);
    	add(contLa);add(js);
 
    	pwdLa=new JLabel("비밀번호",JLabel.CENTER);
    	pwdPf=new JPasswordField();
    	pwdLa.setBounds(120, 540, 80, 30);
    	pwdPf.setBounds(205, 540, 150, 30);
    	add(pwdLa);add(pwdPf);
    	
    	b1=new JButton("수정");
    	b2=new JButton("취소");
    	
    	JPanel p=new JPanel();
    	p.add(b1);p.add(b2);
    	p.setBounds(320,600,300,35);
    	add(p);
    	
    	b1.addActionListener(this);
    	b2.addActionListener(this);
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==b2)
		{
			ctrP.card.show(ctrP, "BOARD_DT");
		}
		else if(e.getSource()==b1)
		{
			String name=nameTf.getText();
			if(name.length()<1) // NOT NULL => 강제로 입력 => 웹(유효성 검사 => 자바스크립트)
			{
			    nameTf.requestFocus();
			    return;
			}
			String subject=subTf.getText();
			if(name.length()<1) // NOT NULL => 강제로 입력 => 웹(유효성 검사 => 자바스크립트)
			{
			    subTf.requestFocus();
			    return;
			}
			String content=ta.getText();
			if(name.length()<1) // NOT NULL => 강제로 입력 => 웹(유효성 검사 => 자바스크립트)
			{
			    ta.requestFocus();
			    return;
			}
			String pwd=String.valueOf(pwdPf.getPassword());
			//     char[] => String으로 변환
			if(pwd.length()<1)
			{
				pwdPf.requestFocus();
				return;
			}
			
			// 데이터를 모아서 DAO로 전송
			BoardVO vo=new BoardVO();
			vo.setName(name);
			vo.setSubject(subject);
			vo.setContent(content);
			vo.setPwd(pwd);
			vo.setNo(no);
			
			// 데이터베이스 연동
			boolean bCheck=dao.boardUpdate(vo);
			// 이동
			if(bCheck==true)
			{
				JOptionPane.showMessageDialog(this, "수정이 완료되었습니다");
				ctrP.boardDTP.print(no);
				ctrP.card.show(ctrP, "BOARD_DT");
			}
			else
			{
				JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다\n다시 입력하세요");
				pwdPf.setText("");
				pwdPf.requestFocus();
			}
			// => response.sendRedirect("detail.jsp");
			
			
			
			
		}
	}
}


