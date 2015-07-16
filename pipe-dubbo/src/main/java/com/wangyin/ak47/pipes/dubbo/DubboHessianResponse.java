package com.wangyin.ak47.pipes.dubbo;

import com.wangyin.ak47.pipes.dubbo.DubboHeader;


/**
 * 
 * @author wyhubingyin
 */
public class DubboHessianResponse {
	private DubboHeader dubboHeader;
	private Object result;

	public DubboHessianResponse() {
	    dubboHeader = new DubboHeader();
	}
	

	public DubboHeader getDubboHeader() {
		return dubboHeader;
	}

	public void setDubboHeader(DubboHeader dubboHeader) {
		this.dubboHeader = dubboHeader;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
