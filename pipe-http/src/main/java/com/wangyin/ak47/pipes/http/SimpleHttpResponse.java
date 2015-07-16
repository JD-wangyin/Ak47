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
    
    /**
     * A Simple Http Response with default params.
     */
    public SimpleHttpResponse(){
        this(null, null, null);
        addHeader("Server", Ak47Constants.NAME+"/"+Ak47Constants.VERSION);
    }
    
    /**
     * A Simple Http Response with given statusCode.
     * 
     * @param statusCode        Status code, such as 200
     */
    public SimpleHttpResponse(String statusCode){
        this(statusCode, null, null);
    }
    
    /**
     * A Simple Http Response with given statusCode and reasonPhrase.
     * 
     * @param statusCode        Status code, such as 200
     * @param reasonPhrase      Reason phrase
     */
    public SimpleHttpResponse(String statusCode, String reasonPhrase){
        this(statusCode, reasonPhrase, null);
    }
    
    /**
     * A Simple Http Response with given statusCode, reasonPhrase and httpVersion.
     * 
     * @param statusCode        Status code, such as 200
     * @param reasonPhrase      Reason phrase, such as OK
     * @param httpVersion       Http version
     */
    public SimpleHttpResponse(String statusCode, String reasonPhrase, String httpVersion){
        setResponseLine(statusCode, reasonPhrase, httpVersion);
    }
    
    /**
     * 
     * @return          Http version
     */
    public String getHttpVersion() {
        return httpVersion;
    }
    /**
     * 
     * @param httpVersion   Http version
     */
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        updateStartLine();
    }
    /**
     * 
     * @return              Status code, such as 200
     */
    public String getStatusCode() {
        return statusCode;
    }
    /**
     * 
     * @param statusCode    Status code, such as 200
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        updateStartLine();
    }
    /**
     * 
     * @return              Reason phrase, such as OK
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    /**
     * 
     * @param reasonPhrase  Reason phrase, such as OK
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
        updateStartLine();
    }
    
    
    /**
     * set Response-Line
     * 
     * @param statusCode        Status code, such as 200
     * @param reasonPhrase      Reason phrase, such as OK
     * @param httpVersion       Http version
     */
    public void setResponseLine(String statusCode, String reasonPhrase, String httpVersion){
        this.statusCode = statusCode==null?this.statusCode:statusCode;
        this.reasonPhrase = reasonPhrase==null?this.reasonPhrase:reasonPhrase;
        this.httpVersion = httpVersion==null?this.httpVersion:httpVersion;
        updateStartLine();
    }
    
    private void updateStartLine(){
        setStartLine(this.httpVersion + " " + this.statusCode + " " + this.reasonPhrase);
    }
    
    /**
     * 转化，如果格式不对则返回null
     * 
     * @param shm       SimpleHttpMessage
     * @return          HttpResponse or null if Response-Line malformed 
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

    
    /**
     * 
     * Deep copy with Response-Line, headers and body.
     * 
     * @param response      HttpResponse
     */
    public void copyOf(SimpleHttpResponse response){
        this.setResponseLine(response.getStatusCode(), response.getReasonPhrase(), 
                response.getHttpVersion());
        this.setHeaders(response.getHeaders());
        this.setContent(response.getContent());
    }
    
}
