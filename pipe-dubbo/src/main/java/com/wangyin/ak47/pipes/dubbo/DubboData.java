package com.wangyin.ak47.pipes.dubbo;


/**
 * 
 * 
 * @author wyhanyu
 *
 */
public class DubboData {

    private DubboHeader dubboHeader;
    private byte[] body;

	public DubboData() {
	    dubboHeader = new DubboHeader();
	    body = null;
	}
	

	public DubboHeader getDubboHeader() {
        return dubboHeader;
    }
    public void setDubboHeader(DubboHeader dubboHeader) {
        this.dubboHeader = dubboHeader;
    }
    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }
    
    
}
