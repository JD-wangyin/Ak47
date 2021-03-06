package com.wangyin.ak47.pipes.http;

import com.wangyin.ak47.common.Ak47Constants;
import com.wangyin.ak47.common.Logger;


/**
 * 简单的HttpRequest实现
 * 
        Request       = Request-Line              
                        *(( general-header        
                         | request-header         
                         | entity-header ) CRLF)  
                        CRLF
                        [ message-body ]          
 * 
 * 
 * 
 * @author hannyu
 *
 */
public class SimpleHttpRequest extends SimpleHttpMessage {
    private static final Logger log = new Logger(SimpleHttpRequest.class);
    
    private String method = "GET";
    // 默认1.0是为了避免 chunked编码，但现在已支持chunked，所以可以直接1.1了
    private String httpVersion = "HTTP/1.1";
    private String url = "/";
    
    
    /**
     * A Simple Http Request with default params.
     */
    public SimpleHttpRequest(){
        this(null,null,null);
        addHeader("User-Agent", Ak47Constants.NAME+"/"+Ak47Constants.VERSION+
                " ("+SimpleHttpRequest.class.getName()+")");
    }
    
    /**
     * A Simple Http Request with given method and url.
     * 
     * @param method        Method in Request-Line
     * @param url           Url in Request-Line
     */
    public SimpleHttpRequest(String method, String url){
        this(method, url, null);
    }
    
    /**
     * A Simple Http Request with given method, url and version.
     * 
     * @param method        Method in Request-Line
     * @param url           Url in Request-Line
     * @param httpVersion   Version in Request-Line
     */
    public SimpleHttpRequest(String method, String url, String httpVersion){
        setRequestLine(method, url, httpVersion);
    }
    
    /**
     * 
     * @return          Method in Request-Line
     */
    public String getMethod() {
        return method;
    }
    /**
     * 
     * @param method    Method in Request-Line
     */
    public void setMethod(String method) {
        this.method = method;
        updateStartLine();
    }
    /**
     * 
     * @return          HttpVersion in Request-Line
     */
    public String getHttpVersion() {
        return httpVersion;
    }
    /**
     * 
     * @param httpVersion   HttpVersion in Request-Line
     */
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        updateStartLine();
    }
    /**
     * 
     * @return          Url in Request-Line
     */
    public String getUrl() {
        return url;
    }
    /**
     * 
     * @param url       Url in Request-Line
     */
    public void setUrl(String url) {
        this.url = url;
        updateStartLine();
    }
    
    /**
     * set Request-Line
     * 
     * @param method        Method in Request-Line
     * @param url           Url in Request-Line
     * @param httpVersion   Version in Request-Line
     */
    public void setRequestLine(String method, String url, String httpVersion){
        this.method = method==null?this.method:method;
        this.url = url==null?this.url:url;
        this.httpVersion = httpVersion==null?this.httpVersion:httpVersion;
        updateStartLine();
    }
    
    private void updateStartLine(){
        setStartLine(this.method + " " + this.url + " " + this.httpVersion);
    }
    
    /**
     * 转化，如果格式不对则返回null
     * 
     * @param shm       SimpleHttpMessage
     * @return          HttpRequest or null if Request-Line malformed 
     */
    public static SimpleHttpRequest valueOf(SimpleHttpMessage shm){
      String startLine = shm.getStartLine();
      String[] startLineItems = startLine.split(" ",3);
      if( startLineItems.length != 3 ){
          log.warn("Malformed first line.");
          return null;
      }else if( !startLineItems[2].startsWith("HTTP") ){
          log.warn("Malformed first line, may NOT http.");
          return null;
      }
      SimpleHttpRequest shr = new SimpleHttpRequest();
      shr.setRequestLine(startLineItems[0], startLineItems[1], startLineItems[2]);
      shr.setHeaders(shm.getHeaders());
      shr.setContent(shm.getContent());
      return shr;
    }

    /**
     * Deep copy with Request-Line, headers and body.
     * 
     * @param request       HttpRequest
     */
    public void copyOf(SimpleHttpRequest request){
        this.setRequestLine(request.getMethod(), request.getUrl(), 
                request.getHttpVersion());
        this.setHeaders(request.getHeaders());
        this.setContent(request.getContent());
    }
}
