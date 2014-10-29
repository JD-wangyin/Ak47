package com.wangyin.ak47.pipes.dubbo;

import com.wangyin.ak47.pipes.dubbo.DubboHeader;


/**
 * 
 * @author wyhubingyin
 * @date 2014年3月18日
 */
public class DubboHessian2Response {
	private DubboHeader dubboHeader;
	private Object result;

	public DubboHessian2Response() {
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
