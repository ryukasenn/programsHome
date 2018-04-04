package com.cn.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.cn.lingrui.common.utils.CommonUtil;

public class Encrypt {

	
	public void getRandom() {
		
		// 定义随机字母
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		List<String> result = new ArrayList<>();
		
		// 获取随机字母
		for(int i = 0; i < 900000 ; i++) {

			result.add(String.valueOf(letters.charAt(new Random().nextInt(52))));
		}
		
		List<Integer> jishuqi = new ArrayList<>();
		List<String> checkItems = new ArrayList<>();
		
		for(String s : result) {
		
			if(checkItems.contains(s)) {
				
				jishuqi.set(checkItems.indexOf(s), jishuqi.get(checkItems.indexOf(s))+1);
			} else {
				
				Integer i = 1;
				jishuqi.add(i);
				checkItems.add(s);
			}
		}
		for(String s : checkItems) {
			
			System.out.println(s + "出现了:" + jishuqi.get(checkItems.indexOf(s)) + "次");
		}
//		return null;
	}
	
	public void ascChekc() {
		
		System.out.println((int) '1');
	}

	@Test
	public void encode() {

//		char head = CommonUtil.getRandomLetter(0);
//		char foot = CommonUtil.getRandomLetter(0);
//		
//		String mima = "hehelong123";
//		List<Character> ss = new ArrayList<>();
//		ss.add(foot);
//		int vinum = 0;
//		for(int i = 0; i < mima.length(); i++) {
//
//			vinum += 1;
//			char currentLetter = mima.charAt(i);
//
////			char temp = (char) (((int) first - firstAsc) * 26 + sec - 97 - vicz);
//			char sec = CommonUtil.getRandomLetter(0);
////			System.out.println(sec);
//			int first = ((int)currentLetter + 97 + (int)foot - (int)head - (int)sec) / 26 + (int)head;
////			System.out.println((char) first);
//			
//
//			if (vinum / 2 != (vinum + 1) / 2) {
//
//				ss.add(sec);
//				ss.add((char)first);
//			} else {
//				ss.add((char)first);
//				ss.add(sec);
//			}
//		}
//		ss.add(head);
//		Collections.reverse(ss);
//		String code = "";
//		for(char s : ss) {
//			
//			code += s;
//		}
//		System.out.println(code);
//		System.out.println(CommonUtil.decodeRs(code));
		System.out.println(CommonUtil.decodeRs("bdsqddprdgbdgfqdgfxu"));
	}
	
	
}





























