package com.wangyin.ak47.pipes.dubbo;

import com.wangyin.ak47.common.ByteUtil;
import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.common.YmlUtil;
import com.wangyin.ak47.core.Pipe;
import com.wangyin.ak47.core.Request;
import com.wangyin.ak47.core.Response;
import com.wangyin.ak47.core.Buffer;
import com.wangyin.ak47.core.exception.Ak47RuntimeException;
import com.wangyin.ak47.pipes.dubbo.DubboHessian2Request;

/**
 * 所有dubbo协议头的接口请继承此Pipe。
 * 
 * dubbo协议头
 * 参见：\dubbo\dubbo-remoting\dubbo-remoting-api\src\main\java\com\alibaba\dubbo\remoting\exchange\codec\ExchangeCodec.java
 * 
 * 一共16个字节：
 * [0..1]   : magic number  (short) 0xdabb
 * [2]      : flag (byte) bit map: FLAG_REQUEST/FLAG_EVENT/FLAG_TWOWAY/HEARTBEAT_EVENT/SERIALIZATION_MASK/...
 * [3]      : status (byte) 20 means OK
 * [4..11]  : request id (long)
 * [12..15] : body length (int)
 * 
 * 
 * @author wyhanyu
 * 
 */
public abstract class AbstractDubboPipe<Q, R> extends Pipe<Q, R> {
	private static final Logger log = new Logger(AbstractDubboPipe.class);

    public static final byte STATUS_OK = 20;
    public static final short MAGIC_NUMBER = (short) 0xdabb;
    public static final int HEADER_LENGTH = 16;
    
	// message flag.
    public static final byte FLAG_REQUEST = (byte) 0x80;
    public static final byte FLAG_TWOWAY = (byte) 0x40;
    public static final byte FLAG_EVENT = (byte) 0x20;
    public static final int SERIALIZATION_MASK = 0x1f;
	
    
    @Override
    public void decodeRequest(Buffer buf, Request<Q> request) throws Exception {
        
        DubboData dd = new DubboData();
        if( !decodeDubboData(buf, dd) ){
            return;
        }
        decodeDubboRequest(dd, request);
        
        // setAttr requestId
        request.setRequestAttr("requestId", dd.getDubboHeader().getRequestId());
    }
    
    /**
     * DubboData 包含了 dubbo header 以及 body
     * 
     * @param dd
     * @param pojos
     * @throws Exception 
     */
    public abstract void decodeDubboRequest(DubboData dd, Request<Q> request) throws Exception;
    
    
    @Override
    public void encodeRequest(Request<Q> request, Buffer buf) throws Exception { 

        DubboData dd = new DubboData();
        encodeDubboRequest(request, dd);
        
        DubboHeader dh = dd.getDubboHeader();
        // set RequestId
        if( dh.getRequestId() == 0 ){
            @SuppressWarnings("unchecked")
            Request<DubboHessian2Request> lastreq = (Request<DubboHessian2Request>) request.getLast();
            if( null != lastreq ){
                dh.setRequestId( lastreq.getPojo().getDubboHeader().getRequestId() + 1 );
            }
        }

        // set bodylength
        dh.setBodyLength(dd.getBody().length);
        
        encodeDubboData(dd, buf);
        
    }
    
    
    /**
     * 将 Q 转化为 DubboData
     * 
     * @param pojos
     * @param dd
     * @throws Exception 
     */
    public abstract void encodeDubboRequest(Request<Q> request, DubboData dd) throws Exception;
    
    @Override
    public void decodeResponse(Buffer buf, Response<R> response) throws Exception {
        
        DubboData dd = new DubboData();
        if( !decodeDubboData(buf, dd) ){
            return;
        }
        decodeDubboResponse(dd, response);
    }
    
    /**
     * DubboData 包含了 dubbo header 以及 body
     * 
     * @param dd
     * @param pojos
     * @throws Exception 
     */
    public abstract void decodeDubboResponse(DubboData dd, Response<R> response) throws Exception;
    
    
    @Override
    public void encodeResponse(Response<R> response, Buffer buf) throws Exception {
        
        DubboData dd = new DubboData();
        encodeDubboResponse(response, dd);
        
        DubboHeader dh = dd.getDubboHeader();
        // set status
        if( dh.getStatus() == 0 ){
            dh.setStatus(STATUS_OK);
        }
        
        // set back RequestId
//        @SuppressWarnings("unchecked")
//        Request<DubboHessian2Request> nowreq = (Request<DubboHessian2Request>) response.getRequest();
//        if( null != nowreq ){
//            log.error("encodeResponse requestId: {}", nowreq.getPojo().getDubboHeader().getRequestId());
//            dh.setRequestId( nowreq.getPojo().getDubboHeader().getRequestId() );
//        }
        Long requestId = (Long) response.getRequestAttr("requestId");
        if( requestId != null ){
            log.error("encodeResponse requestId: {}", requestId);
            dh.setRequestId(requestId);
        }
        
        
        // set bodylength
        dh.setBodyLength(dd.getBody().length);
        
        encodeDubboData(dd, buf);
    }
    
    /**
     * 将 Q 转化为 DubboData
     * 
     * @param pojos
     * @param dd
     * @throws Exception 
     */
    public abstract void encodeDubboResponse(Response<R> response, DubboData dd) throws Exception;
    
    
    /**
     * 解析 Dubbo 协议头
     * 
     * @param in
     * @return 
     */
    private boolean decodeDubboData(Buffer buf, DubboData dd){
        int readableBytes = buf.readableBytes();
        
        // header length
        if( readableBytes < HEADER_LENGTH ){
            log.debug("Need more read for header.");
            return false;
        }
        
        byte[] bytes = new byte[readableBytes];
        int readerIndex = buf.readerIndex();
        buf.readBytes(bytes);
        
        // magic number
        short magicNumber = ByteUtil.bytes2short(bytes);
        if( magicNumber != MAGIC_NUMBER ){
            log.error("Magic number fail, close connection.");
            // NOT rollback
            throw new Ak47RuntimeException("Wrong magic number["+magicNumber+"] expected ["+MAGIC_NUMBER+"]");
        }
        
        // body length
        int bodyLength = ByteUtil.bytes2int(bytes, 12);
        if( bytes.length < HEADER_LENGTH + bodyLength ){
            log.debug("Need more read for body.");
            buf.readerIndex(readerIndex);
            return false;
        }
        
        // set header
        DubboHeader dh = dd.getDubboHeader();
        dh.setMagicNumber(magicNumber);
        dh.setFlag(bytes[2]);
        dh.setStatus(bytes[3]);
        dh.setRequestId(ByteUtil.bytes2long(bytes,4));
        dh.setBodyLength(bodyLength);
        
        // set body
        dd.setBody(ByteUtil.copyOf(bytes, HEADER_LENGTH, bodyLength));
        
        // pipeline
        if( bytes.length > HEADER_LENGTH + bodyLength ){
            log.debug("Detect pipeline.");
            buf.readerIndex(readerIndex + HEADER_LENGTH + bodyLength);
        }
        
//        log.error("decodeDubboData: \n{}", YmlUtil.obj2PrettyYml(dd));
        return true;
    }
    
    /**
     * 将DubboData转化为bytes写入out中
     * 
     * @param out
     * @param dd
     */
    private boolean encodeDubboData(DubboData dd, Buffer buf){
        
        DubboHeader dh = dd.getDubboHeader();
        byte[] headerBytes = new byte[HEADER_LENGTH];
        
        byte[] magicNumber = ByteUtil.short2bytes( dh.getMagicNumber() );
        ByteUtil.copy(magicNumber, 0, headerBytes, 0, 2);
        
        byte flag = dh.getFlag();
        ByteUtil.copy(flag,headerBytes,2);
        
        byte status = dh.getStatus();
        ByteUtil.copy(status, headerBytes, 3);
        
        byte[] requestId = ByteUtil.long2bytes( dh.getRequestId() );
        ByteUtil.copy(requestId, 0, headerBytes, 4, 8);
        
        byte[] bodyLength = ByteUtil.int2bytes( dh.getBodyLength() );
        ByteUtil.copy(bodyLength, 0, headerBytes, 12, 4);
        
        buf.writeBytes(headerBytes);
        buf.writeBytes(dd.getBody());
        
//        log.error("encodeDubboData: \n{}", YmlUtil.obj2PrettyYml(dd));
        return true;
    }
    
    
    @Override
    public Buffer[] split(Buffer buf){
        //FIXME split
        
        int readerIndex = buf.readerIndex();
        int readableBytes = buf.readableBytes();
        
        log.error("preDecodeRequest: readable {}.", readableBytes);
        
        // header length
        if( readableBytes < HEADER_LENGTH ){
            log.debug("Need more read for header.");
            return null;
        }
        
        // read header
        byte[] headerBytes = new byte[HEADER_LENGTH];
        buf.getBytes(readerIndex, headerBytes);
        
        int bodyLength = ByteUtil.bytes2int(headerBytes, 12);
        int reqLength = HEADER_LENGTH + bodyLength;
        
        //FIXME
        log.error("requestId: {}, reqLength: {}", ByteUtil.bytes2long(headerBytes, 4), reqLength);
        
        
        if( readableBytes < reqLength ){
            log.debug("Need more read for body.");
            return null;
        }else{
            Buffer copybuf = buf.copy(readerIndex, reqLength);
            buf.readerIndex(readerIndex + reqLength);
            return copybuf;
        }
        
    }
    
    @Override
    public void doService(Request<Q> request, Response<R> response) throws Exception {
        
    }

}
