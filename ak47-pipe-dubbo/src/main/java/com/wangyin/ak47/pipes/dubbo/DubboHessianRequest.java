package com.wangyin.ak47.pipes.dubbo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wangyin.ak47.pipes.dubbo.DubboHeader;
/**
 * 
 * @author wyhubingyin
 * @date 2014年3月18日
 */
public class DubboHessianRequest {

	private DubboHeader dubboHeader;
	private String dubboVersion;
    private String service;
    private String version;
	private String method;
    private List<Object> args;
	private Map<String, String> attachments; 

	public DubboHessianRequest() {
	    dubboHeader = new DubboHeader();
	    args = new LinkedList<Object>();
	    attachments = new LinkedHashMap<String, String>();
	}

    public DubboHeader getDubboHeader() {
        return dubboHeader;
    }

    public void setDubboHeader(DubboHeader dubboHeader) {
        this.dubboHeader = dubboHeader;
    }

    public String getDubboVersion() {
        return dubboVersion;
    }

    public void setDubboVersion(String dubboVersion) {
        this.dubboVersion = dubboVersion;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }
	
	
}
