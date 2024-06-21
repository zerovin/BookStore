package com.sist.dao;
/*
 CNO                                       NOT NULL NUMBER
 ID                                                 VARCHAR2(20)
 GNO                                                NUMBER
 ACCOUNT                                            NUMBER
 PRICE                                              NUMBER
 REGDATE                                            DATE 
 */
import java.util.*; //자바<=>오라클 - 데이터를 모아서 관리, 송수신
import lombok.Data;
// VO = DTO : Data Transfer Object;
@Data
public class CartVO {
	private int bno, bnum, account, price;
	private String id;
	private Date regdate;
	private WikiVO wvo=new WikiVO();
	//조인, 서브쿼리 => 해당 VO를 포함
	//VO는 반드시 테이블 한개만 연결
}
