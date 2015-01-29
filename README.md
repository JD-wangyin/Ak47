Ak47 
=========

Ak47 is an easy [Network Service](http://en.wikipedia.org/wiki/Network_service) Testing Framework. It is a Test-Specific Framework, designed for better [Automation-Testing](http://en.wikipedia.org/wiki/Test_automation), [Performance-Testing](http://en.wikipedia.org/wiki/Performance_testing), [Mock-Testing](http://en.wikipedia.org/wiki/Mock_object) and with the same code style.

Ak47 can support almost all network protocols, such as [HTTP](http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol), [Hession](http://hessian.caucho.com/), [ISO8583](http://en.wikipedia.org/wiki/ISO_8583), [Dubbo](https://github.com/alibaba/dubbo), [Thrift](https://thrift.apache.org/), and other [RPC](http://en.wikipedia.org/wiki/Remote_procedure_call) protocols. 

Ak47 is base on [Netty](http://netty.io)/[NIO](http://en.wikipedia.org/wiki/Non-blocking_I/O_(Java)), and its design philosophy is simple to use, scalable, and high performance.

## WIKI

Welcome to the AK47 wiki. Please head to [\[User Guide\]](User-Guide).

欢迎来到Ak47主页，请转到[\[用户指南\]](用户指南)。

## Examples
A Simple Http Driver
```java
// new a Pipe
SimpleHttpPipe pipe = new SimpleHttpPipe();

// create a simple driver
SimpleDriver<SimpleHttpRequest, SimpleHttpResponse> driver = 
        pipe.createSimpleDriver("www.jd.com", 80);

// prepare a request
SimpleHttpRequest httpreq = new SimpleHttpRequest("GET", "/");

// send it, and get a response
SimpleHttpResponse httpres = driver.send(httpreq);

// finally check the res.
System.out.println(httpres.getStatusCode());
System.out.println(httpres.getContent().length);
```
A Simple Http Stub
```java
// new a Pipe
SimpleHttpPipe pipe = new SimpleHttpPipe();

// create a stub
SimpleStub<SimpleHttpRequest, SimpleHttpResponse> stub = pipe.createSimpleStub(8055);

// add a service
stub.addService("myservice", new Service<SimpleHttpRequest, SimpleHttpResponse>(){
    @Override
    public void doService(Request<SimpleHttpRequest> request,
            Response<SimpleHttpResponse> response) throws Exception {
        SimpleHttpRequest httpreq = request.pojo();
        
        SimpleHttpResponse httpres = new SimpleHttpResponse();
        String content = "Hello Ak47! Your request url is "+httpreq.getUrl();
        httpres.setContent(content.getBytes());
        response.pojo(httpres);
    }
});

// finally start and hold it.
stub.start();
stub.hold();
```


## How to build

You require the following to build AK47:

* Latest stable [Oracle JDK 7](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Maven](http://maven.apache.org/)

Note that this is build-time requirement.  JDK 6 is enough to run your AK47-based application.



## Contributing 

Ak47 is free software/open source, and is distributed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Please feel free to contribute code or documentation. [Pull Requests](https://help.github.com/articles/using-pull-requests/) are welcome!


## Questions?

You can try [creating an issue](https://help.github.com/articles/creating-an-issue/), or 
[searching issues](https://help.github.com/articles/searching-issues/), Or contact to [hannyu](https://github.com/hannyu).

