package com.sist.temp;
import java.util.ArrayList;
import java.util.Scanner;
public class ZipMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZipcodeDAO dao=ZipcodeDAO.newInstance();
		Scanner scan=new Scanner(System.in);
		System.out.print("검색어 입력 >> ");
		String dong=scan.next();
		ArrayList<ZipcodeVO> list=dao.zipcodeFind(dong);		
		for(ZipcodeVO vo:list) {
			System.out.println(vo.getZipcode()+" "
					+vo.getSido()+" "
					+vo.getGugun()+" "
					+vo.getDong()+" "
					+vo.getBunji());
		}
	}

}
