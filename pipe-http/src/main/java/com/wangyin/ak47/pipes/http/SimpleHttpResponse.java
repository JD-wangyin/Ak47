package com.wangyin.ak47.pipes.http;


import com.wangyin.ak47.common.Ak47Constants;
import com.wangyin.ak47.common.Logger;


/**
 * 简单的HttpResponse实现
 * 
 *          Response = Status-Line                  
                       *(( general-header        
                        | response-header        
                        | entity-header ) CRLF)  
                       CRLF
                       [ message-body ]          
 * 
 * 
 * 
 * @author wyhanyu
 *
 */
public class SimpleHttpResponse extends SimpleHttpMessage {
    private static final Logger log = new Logger(SimpleHttpResponse.class);
    
    private String httpVersion = "HTTP/1.1";
    private String statusCode = "200";
    private String reasonPhrase = "OK";
    

    public SimpleHttpResponse(){
        this(null, null, null);
        addHeader("Server", Ak47Constants.NAME+"/"+Ak47Constants.VERSION);
    }
    public SimpleHttpResponse(String statusCode){
        this(null, statusCode, null);
    }
    public SimpleHttpResponse(String statusCode, String reasonPhrase){
        this(null, statusCode, reasonPhrase);
    }
    public SimpleHttpResponse(String httpVersion, String statusCode, String reasonPhrase){
        setResponseLine(httpVersion, statusCode, reasonPhrase);
    }
    
    

    public String getHttpVersion() {
        return httpVersion;
    }
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        updateStartLine();
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        updateStartLine();
    }
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
        updateStartLine();
    }
    
    /**
     * 设置 第一行
     * 
     * @param method
     * @param url
     * @param httpVersion
     */
    public void setResponseLine(String httpVersion, String statusCode, String reasonPhrase){
        this.httpVersion = httpVersion==null?this.httpVersion:httpVersion;
        this.statusCode = statusCode==null?this.statusCode:statusCode;
        this.reasonPhrase = reasonPhrase==null?this.reasonPhrase:reasonPhrase;
        updateStartLine();
    }
    
    private void updateStartLine(){
        setStartLine(this.httpVersion + " " + this.statusCode + " " + this.reasonPhrase);
    }
    
    /**
     * 转化，如果格式不对则返回null
     * 
     * @param shm
     * @return
     */
    public static SimpleHttpResponse valueOf(SimpleHttpMessage shm){
      String startLine = shm.getStartLine();
      String[] startLineItems = startLine.split(" ",3);
      if( startLineItems.length != 3 ){
          log.warn("Malformed first line.");
          return null;
      }else if( !startLineItems[0].startsWith("HTTP") ){
          log.warn("Malformed first line, may NOT http.");
          return null;
      }
      SimpleHttpResponse shr = new SimpleHttpResponse();
      shr.setResponseLine(startLineItems[0], startLineItems[1], startLineItems[2]);
      shr.setHeaders(shm.getHeaders());
      shr.setContent(shm.getContent());
      return shr;
    }

    
    public void copyOf(SimpleHttpResponse response){
        this.setResponseLine(response.getHttpVersion(), response.getStatusCode(), 
                response.getReasonPhrase());
        this.setHeaders(response.getHeaders());
        this.setContent(response.getContent());
    }
}
