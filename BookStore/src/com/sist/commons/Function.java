package com.sist.commons;
//서버 => 클라이언트 통신 => 내부 프로토콜
// 문자,숫자 가능하나 숫자가 보안에 유리
public class Function {
	public static final int LOGIN=100; //웹에서 login.jsp, 파일명은 중복불가라서 파일로 구분
	public static final int MYLOG=110;
	public static final int CHAT=200;
	public static final int ONETOONE=300;
	public static final int ONEINIT=310;
	public static final int ONENO=320;
	public static final int ONEYES=330;
	public static final int ONEEXIT=340;
	public static final int INFO=400;
	public static final int SEND=500;
	public static final int EXIT=600;
	public static final int MYEXIT=610;
}
