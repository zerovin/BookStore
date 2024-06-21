package com.sist.temp;

import lombok.Data;

//우편번호 검색
/*
 *  VO => desc 테이블명
 *  메소드 제작
 *  MainClass 동입력 => 출력 
 */

/*
 ZIPCODE     VARCHAR2(7)
 SIDO        VARCHAR2(50)
 GUGUN       VARCHAR2(50)
 DONG        VARCHAR2(100)
 BUNJI       VARCHAR2(100)
 */
@Data
public class ZipcodeVO {
	private String zipcode, sido, gugun, dong, bunji;
}
