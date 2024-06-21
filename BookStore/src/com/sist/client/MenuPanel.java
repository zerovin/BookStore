package com.sist.client;
import java.awt.*;
import javax.swing.*;
public class MenuPanel extends JPanel{
	JButton homeBtn, findBtn, newsBtn, boardBtn, chatBtn, mypageBtn, exitBtn;
	public MenuPanel() {
		setLayout(new GridLayout(1, 6, 5, 5)); //가로메뉴
		//setLayout(new GridLayout(6, 1, 5, 5));  //세로메뉴
		homeBtn=new JButton("홈");
		findBtn=new JButton("도서검색");
		newsBtn=new JButton("뉴스");
		boardBtn=new JButton("게시판");
		chatBtn=new JButton("채팅");
		mypageBtn=new JButton("마이페이지");
		exitBtn=new JButton("나가기");
		
		add(homeBtn); //add는 쓰는 순서대로 들어간다
		add(findBtn);
		add(newsBtn);
		add(boardBtn);
		add(chatBtn);
		add(mypageBtn);
		add(exitBtn);
	}
}
