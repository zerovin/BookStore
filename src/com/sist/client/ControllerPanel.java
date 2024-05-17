package com.sist.client;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;
public class ControllerPanel extends JPanel{
	public CardLayout card=new CardLayout();
	public HomePanel hp=new HomePanel();
	public FindPanel fp=new FindPanel();
	public NewsPanel np=new NewsPanel();
	public ReviewPanel rp=new ReviewPanel();
	public ChatPanel chp=new ChatPanel();

	/*
	public WriteReview write=new WriteReview();
	public DetailPanel detail=new DetailPanel();
	public EditReview edit=new EditReview();
	public DeleteCheckPanel delete=new DeleteCheckPanel();
	*/ 
	
	public ControllerPanel() {
		setLayout(card);
		add("HOME",hp);
		add("FIND",fp);
		add("NEWS",np);
		add("REVIEW", rp);
		add("CHAT", chp);
		
		/*
		add("WRITE", write);
		add("DETAIL", detail);
		add("EDIT", edit);
		add("DELETE", delte);
		*/
	}
}
