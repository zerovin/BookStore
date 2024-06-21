package com.sist.client;

import javax.swing.*;
import java.net.*;
import com.sist.dao.*;
import com.sist.commons.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

//ISBN, BOOKNAME, WRITER, TRANSLATOR, PAGE, PRICE, PUBDATE, SERIES, IMAGE
public class BookDetailPanel extends JPanel implements ActionListener,ItemListener{
	JLabel imageLa,booknameLa,writerLa,transLa,pageLa,priceLa,pubdateLa,seriesLa;
	JButton cartBtn,listBtn;
	JComboBox<Integer> box;
	JLabel numLa,totalpriceLa, noteLa;
	ControllPanel ctrP;// 화면 이동
	WikiDAO dao;
	int BNUM=0;
	String myId;
	public BookDetailPanel(ControllPanel ctrP)
	  {
		dao=WikiDAO.newInstance();
		this.ctrP = ctrP;
		setLayout(null);
		imageLa = new JLabel();
		imageLa.setBounds(10,15,450,550);
		add(imageLa);
		
		booknameLa = new JLabel();
		booknameLa.setBounds(470, 15, 440, 110);
		booknameLa.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		add(booknameLa);
	
		writerLa = new JLabel();
		writerLa.setBounds(470,125,400,30);
		writerLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(writerLa);
		
		transLa = new JLabel();
		transLa.setBounds(470, 155, 400, 30);
		transLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(transLa);
				
		pageLa = new JLabel();
		pageLa.setBounds(470, 190, 200, 30);
		pageLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(pageLa);
				
		priceLa = new JLabel();
		priceLa.setBounds(470, 225, 400, 30);
		priceLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(priceLa);
				
		pubdateLa = new JLabel();
		pubdateLa.setBounds(470, 260, 400, 30);
		pubdateLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(pubdateLa);
				
		seriesLa = new JLabel();
		seriesLa.setBounds(470, 295, 300, 30);
		seriesLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(seriesLa);
				
		numLa = new JLabel("수량 ");
		numLa.setBounds(470, 330, 40, 30);
		numLa.setFont(new Font("맑은 고딕",Font.PLAIN,15));
		add(numLa);
		
		box=new JComboBox<Integer>();
		box.addItem(1);
		box.addItem(2);
		box.addItem(3);
		box.addItem(4);
		box.addItem(5);
		box.addItem(6);
		box.addItem(7);
		box.setBounds(530, 330, 100, 30);
		add(box);
		
		noteLa = new JLabel("최대 구매 가능 수량은 7개입니다."); 
		noteLa.setFont(new Font("맑은 고딕",Font.BOLD,13));
		noteLa.setForeground(Color.red);
		noteLa.setBounds(470, 370, 200, 30);
		add(noteLa);
		
		totalpriceLa = new JLabel();
		totalpriceLa.setBounds(470, 420, 380, 40);
		totalpriceLa.setFont(new Font("맑은 고딕",Font.BOLD,20));
		add(totalpriceLa);
		
		cartBtn = new JButton(" 장바구니 ");
		listBtn = new JButton(" 목록 ");
		JPanel p=new JPanel();
		cartBtn.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		listBtn.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		cartBtn.setBackground(new Color(255,150,157));
		listBtn.setBackground(new Color(156,156,156));
		cartBtn.setForeground(Color.WHITE);
		listBtn.setForeground(Color.WHITE);
		p.add(cartBtn);p.add(listBtn);
		p.setBounds(400, 510, 450, 100);
		add(p);
		
		cartBtn.addActionListener(this);
		listBtn.addActionListener(this);
		box.addItemListener(this);
	  }
	
	public void print(int NUM, String id)
	  {
		 myId=id;
		 BNUM=NUM;
		  // 1. 오라클에서 값을 받는다 
		  WikiVO vo=dao.bookDetailData(NUM);
		  try
		  {
			  URL url=new URL(vo.getImage());
			  Image img=ImageChange.getImage(new ImageIcon(url), 400, 500);
			  imageLa.setIcon(new ImageIcon(img));
		  }catch(Exception ex) {}
		  booknameLa.setText("<html><font size= 5>"+vo.getBookname()+"</font></html>");
		  writerLa.setText("지은이: "+ vo.getWriter());
		  transLa.setText("옮긴이: "+vo.getTranslator());
		  pageLa.setText("페이지: "+vo.getPage());
		  priceLa.setText("가격:  "+new DecimalFormat("#,###,###").format(vo.getPrice())+"원");
		  pubdateLa.setText("발행일: "+vo.getPubdate());
		  seriesLa.setText("시리즈: "+vo.getSeries());
	 }  
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==box)
		{
			String price=priceLa.getText();
			price=price.substring(price.indexOf(":")+1);
			price=price.replaceAll("[^0-9]", ""); // 숫자를 제외한 나머지는 공백으로 바꿔준다
			
			int account=box.getSelectedIndex()+1;
			int total=Integer.parseInt(price)*account;
			
			DecimalFormat df=new DecimalFormat("##,###,###");
			String s=df.format(total);
			totalpriceLa.setText("총 구매 가격: "+s+"원");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==listBtn)
		{
			ctrP.card.show(ctrP, "HOME");
		}
		else if(e.getSource()==cartBtn)
		{
			CartVO vo = new CartVO();
			vo.setBnum(BNUM);
			String id=ctrP.cliMain.myId;
			vo.setId(id);
			int account = box.getSelectedIndex()+1;
			vo.setAccount(account);
			String price = totalpriceLa.getText();
			price = price.replaceAll("[^0-9]", "");
			if(totalpriceLa.getText().isEmpty()) {
				String oneprice=priceLa.getText().replaceAll("[^0-9]", "");
				vo.setPrice(Integer.parseInt(oneprice));
			}else {
				vo.setPrice(Integer.parseInt(price));				
			}
			
			dao.cartInsert(vo);
			JOptionPane.showMessageDialog(this, "장바구니에 추가되었습니다 \n마이페이지에서 확인하세요");
			ctrP.myP.print();
			//이동 => 마이페이지로 이동
			ctrP.card.show(ctrP, "MYPAGE");
			
		}
		
	}
  }

