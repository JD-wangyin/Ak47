package com.wangyin.ak47.pipes.dubbo;


/**
 * dubbo header
 * @author wyhanyu
 *
 */
public class DubboHeader {
    
    private short magicNumber = AbstractDubboPipe.MAGIC_NUMBER;
    private byte flag;
    private byte status;
    private long requestId;
    private int bodyLength;
    
	
	public String toString() {
		return "DubboHeader [magicNumber=" + magicNumber + ", flag=" + flag + ", status=" + status + ", requestId=" + requestId + ", bodyLength=" + bodyLength + "]";
	}

	public short getMagicNumber() {
        return magicNumber;
    }
    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }
    public byte getFlag() {
        return flag;
    }
    public void setFlag(byte flag) {
        this.flag = flag;
    }
    public byte getStatus() {
        return status;
    }
    public void setStatus(byte status) {
        this.status = status;
    }
    public long getRequestId() {
        return requestId;
    }
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
    public int getBodyLength() {
        return bodyLength;
    }
    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}
