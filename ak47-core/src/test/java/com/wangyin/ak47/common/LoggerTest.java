package com.wangyin.ak47.common;

import java.math.BigDecimal;
import java.util.Date;

import org.testng.annotations.Test;

import com.wangyin.ak47.common.Logger;


public class LoggerTest {
	public static final Logger log = new Logger(LoggerTest.class);

	@Test
	public void testDebug(){
	    log.debug("debug: Today is {}.", new Date());
	}
	
	@Test
    public void testInfo(){
	    log.info("info: I win {}$", new BigDecimal(999));
    }
	
	@Test
	public void testWarn(){
	    log.warn("warn: Code[{}] means NOT FOUND. -{}-", 404);
    }
	
	@Test
	public void testError(){
	    log.error("error: When you see this, you fail!", new Exception("其实我是无辜的"));
	}
}
