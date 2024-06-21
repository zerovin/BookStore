package com.sist.dao;
import java.util.*;
import lombok.Data;
@Data
public class WikiVO {
	private int num;
	private long isbn;
	private String bookname, writer, translator;
	private int page, price;
	private Date pubdate;
	private String image;
	private String series, detail;
	private int hit;
}

