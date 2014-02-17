package com.ocr;

import java.io.File;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
			String maybe2 = new OCR().recognizeText(new  File("E:\\dir\\test_file\\zhuanli2.gif"), "gif");
			System.out.println(maybe2);
			System.out.println("**********");
			//MyString str=new MyString();
			//System.out.println(str.getString(maybe2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		SoundServer s=new SoundServer();
//		s.playSound("E:\\111\\HOOK1.wav");
	}

}
