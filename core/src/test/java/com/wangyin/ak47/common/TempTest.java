package com.wangyin.ak47.common;

import org.testng.annotations.Test;

public class TempTest {
    
    
    
    
	
	@Test
	public void testhaha(){
		String[] ss = "123".split("\\n");
		System.out.println(""+ss.length);
		ss = "123\n".split("\\n");
		System.out.println(""+ss.length);
		ss = "123\n456".split("\\n");
		System.out.println(""+ss.length);
		ss = "123\n456\n".split("\\n");
		System.out.println(""+ss.length);
		ss = "\n123\n456".split("\\n");
		System.out.println(""+ss.length);
		
		
	}

}
