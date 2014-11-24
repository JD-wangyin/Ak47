package com.wangyin.ak47.core;



/**
 * Every complete network-interaction each requires experienced 4 times codecs.
 *   * First, Client service POJO(Q) ready , once @see #encodeRequest, 
 *     convert POJO(Q) into binary data (here Buffer), sent. 
 *   * Second, Server receives the binary data, once @see #decodeRequest, convert binary data into POJO(Q), 
 *     then do some biz things. 
 *   * After Server processed data and ready to return POJO(R), once @see #encodeResponse, 
 *     convert POJO(R) into binary data, sent. 
 *   * Finally, Client receives the returned binary data, once @see #decodeResponse, 
 *     convert binary data into POJO(R), The whole interaction is over.
 * 
 * WARNING: Keep the "stateless", because there must be multi-threading problems.
 * 
 * 每一次完整的网络交互都需要经历4次编解码: 
 * 
 *      首先，Client准备好业务数据POJO(Q)，进行一次 encodeRequest，将POJO(Q)转化为二进制数据(这里是Buffer)，发送。
 *      
 *      其次，Server接收到数据，进行一次 decodeRequest，将二进制数据转化为POJO(Q)，再进行业务处理。
 *      
 *      然后，Server处理完之后准备好要返回的数据POJO(R)，进行一次 encodeResponse，将POJO(R)转化为二进制数据，发送。
 *      
 *      最后，Client接收到返回，进行一次 decodeResponse，将二进制数据转化为POJO(R)，整个交互结束。
 *      
 * 注意： 编解码必须保持"无状态"的，因为会有多线程问题。
 * 
 * @author hannyu
 *
 * @param <Q>       the POJO in Request
 * @param <R>       the POJO in Response
 */
public interface Codec<Q, R> {
    
    /**
     * Converting binary data (here Buffer) into POJO(Q), in Server or {@see Stub}.
     * 
     * @param buf
     * @param request
     * @throws Exception
     */
    public void decodeRequest(Buffer buf, Request<Q> request) throws Exception;
    
    
    /**
     * Converting POJO(Q) into binary data (here Buffer), in Client or {@see Driver}.
     * 
     * @param request
     * @param buf
     * @throws Exception
     */
    public void encodeRequest(Request<Q> request, Buffer buf) throws Exception;
    
    
    /**
     * Converting binary data (here Buffer) into POJO(R), in Client or {@see Driver}.
     * 
     * @param buf
     * @param response
     * @throws Exception
     */
    public void decodeResponse(Buffer buf, Response<R> response) throws Exception;

    
    /**
     * Converting POJO(R) into binary data (here Buffer), in Server or {@see Stub}.
     * 
     * @param response
     * @param buf
     * @throws Exception
     */
    public void encodeResponse(Response<R> response, Buffer buf) throws Exception;
    
}
