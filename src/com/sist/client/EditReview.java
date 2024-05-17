package com.sist.client;
import javax.swing.*;
import java.awt.*;
public class EditReview extends JFrame{
	JLabel editTitle=new JLabel("후기 수정",JLabel.CENTER);
	JLabel name,title,content,pw;
	JButton submit,cancel;
	JTextField inputName, inputTitle;
	JTextArea writeContent;
	JPasswordField inputPw;
	
	public EditReview() {
		//초기화
		name=new JLabel("이름");
		title=new JLabel("제목");
		content=new JLabel("내용");
		pw=new JLabel("비밀번호");
		
		submit=new JButton("글쓰기");
		cancel=new JButton("취소");
		inputName=new JTextField();
		inputTitle=new JTextField();
		writeContent=new JTextArea();
		JScrollPane contentWrap=new JScrollPane(writeContent);
		inputPw=new JPasswordField();
		
		//배치
		setLayout(null);
		editTitle.setFont(new Font("맑은 고딕",Font.BOLD,20));
		editTitle.setBounds(0, 15, 800, 20);
		add(editTitle);
		
		name.setBounds(40, 75, 80, 30);
		inputName.setBounds(110, 75, 300, 30);
		add(name);
		add(inputName);
		title.setBounds(40, 115, 80, 30);
		inputTitle.setBounds(110, 115, 300, 30);
		add(title);
		add(inputTitle);
		content.setBounds(40, 155, 80, 30);
		contentWrap.setBounds(110, 155, 620, 285);
		add(content);
		add(contentWrap);
		pw.setBounds(40, 450, 80, 30);
		inputPw.setBounds(110, 450, 300, 30);
		add(pw);
		add(inputPw);
		
		JPanel btn=new JPanel();
		btn.add(submit);
		btn.add(cancel);
		btn.setBounds(0, 500, 800, 35);
		add(btn);
		
		/*
		setSize(800, 600);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		*/ 
	}
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new EditReview();
	}
	*/
	
}
