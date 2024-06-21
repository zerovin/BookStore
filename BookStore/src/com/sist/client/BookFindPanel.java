package com.sist.client;
import java.util.*;
import java.util.List;

import com.sist.dao.*;
import com.sist.commons.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;

public class BookFindPanel extends JPanel implements ActionListener, MouseListener {
	JTable table;
    DefaultTableModel model;
    JTextField tf;
    JButton b,entire,list;
    WikiDAO dao;
    ControllPanel ctrP;
    TableColumn column;
    int curpage=1;
    String myId;
    public BookFindPanel(ControllPanel ctrP) {
    	dao=WikiDAO.newInstance();
        this.ctrP = ctrP;

        setLayout(new BorderLayout());
        tf = new JTextField(35);
        b = new JButton("검색");
        entire = new JButton("전체보기");
        list = new JButton("목록");
        list.setPreferredSize(new Dimension(100, 40)); // 크기를 너비 100, 높이 40으로 설정

        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        p.add(tf);
        p.add(b);
        //p.add(entire);
        p.add(list);
        add("North", p);

        String[] col = {"번호", "표지", "도서명", "지은이", "가격", "시리즈"};
        Object[][] row = new Object[0][6];

        model = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowVerticalLines(false);
        JScrollPane js = new JScrollPane(table);
        add("Center", js);

        for (int i = 0; i < col.length; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0)
                column.setPreferredWidth(10);
            else if (i == 1)
                column.setPreferredWidth(80); // 표지 너비 조정
            else if (i == 2)
                column.setPreferredWidth(300); // 도서명 너비 조정
            else if (i == 3)
                column.setPreferredWidth(100); // 지은이 너비 조정
            else if (i == 4)
                column.setPreferredWidth(80); // 가격 너비 조정
            else if (i == 5)
                column.setPreferredWidth(100); // 시리즈 너비 조정
        }

        tf.addActionListener(this);
        b.addActionListener(this);
        entire.addActionListener(this);
        list.addActionListener(this);
        table.addMouseListener(this);
        table.getTableHeader().setBackground(Color.lightGray);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 오른쪽 정렬로 변경
        buttonPanel.add(list);
        add("South", buttonPanel); // 목록 버튼 패널을 남쪽에 추가
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==tf || e.getSource()==b)
		{
			String name=tf.getText();
			if(name.length()<1)
			{
				JOptionPane.showMessageDialog(this, "검색어를 입력하세요");
				tf.requestFocus();
				return;
			}
			// 데이터베이스 연동 
            ArrayList<WikiVO> list = dao.bookFindData(name);
            if(list.size()<1)
			{
				JOptionPane.showMessageDialog(this, "검색된 결과가 없습니다");
			}
            else
			{
				for(int i=model.getRowCount()-1;i>=0;i--)
				{
					model.removeRow(i);
				}
				for(WikiVO vo:list)
				{
					try
					{
						URL url=new URL(vo.getImage());
						Image img=ImageChange.getImage(new ImageIcon(url), 35, 35);
						Object[] obj={
							vo.getNum(),
							new ImageIcon(img),
							vo.getBookname(),
							vo.getWriter(),
							new DecimalFormat("##,###,###").format(vo.getPrice())+"원",
							vo.getSeries()
						};
						model.addRow(obj);
					}catch(Exception ex){}
				}
			}
		}
         else if (e.getSource()==list) 
        {
        	 ctrP.card.show(ctrP, "HOME"); // 목록 버튼 클릭 시 홈 패널로 화면 전환
        }
        /*else if (e.getSource() == entire) {
            // 전체보기 버튼 클릭 시
            model.setRowCount(0); // 기존 테이블 데이터 모두 제거

            // 데이터베이스에서 모든 도서 데이터 가져오기
            ArrayList<BooksVO> list = dao.BooksAllData(); 

            // 가져온 도서 데이터를 테이블 모델에 추가
            for(BooksVO vo:list)
			{
				try
				{
					URL url=new URL(vo.getImage());
					Image img=ImageChange.getImage(new ImageIcon(url), 35, 35);
					Object[] obj={
						vo.getNum(),
						new ImageIcon(img),
						vo.getBookname(),
						vo.getWriter(),
						vo.getPrice() + "원",
						vo.getSeries()
					};
					model.addRow(obj);
				}catch(Exception ex){}
			}
        }*/
    }
    
    
    @Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==table)
		{
			if(e.getClickCount()==2)
			{
				int row=table.getSelectedRow();
				String no=model.getValueAt(row, 0).toString();
				ctrP.bookDP.print(Integer.parseInt(no),myId);
				ctrP.card.show(ctrP, "DETAIL");
			}
		}
	}
    @Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
}