Ak47 
=========

Ak47 is an easy [Network Service](http://en.wikipedia.org/wiki/Network_service) Testing Framework. It can support mostly network protocols, such as [HTTP](http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol), [Hession](http://hessian.caucho.com/), [ISO8583](http://en.wikipedia.org/wiki/ISO_8583), [Dubbo](https://github.com/alibaba/dubbo), [Thrift](https://thrift.apache.org/), and other [RPC](http://en.wikipedia.org/wiki/Remote_procedure_call) protocols. 

Its most novel feature is the integration of [Performance-Testing](http://en.wikipedia.org/wiki/Performance_testing), [Mock-Testing](http://en.wikipedia.org/wiki/Mock_object) and [Automation-Testing](http://en.wikipedia.org/wiki/Test_automation). By the 3-in-one design, cases can be written always in the same style and be easily migrated in the three scenes.

Ak47 is created and maintained by [JD-wangyin](https://github.com/JD-wangyin), with the help of [many contributors](https://github.com/JD-wangyin/ak47/graphs/contributors).

## Features

- **Simplicity**: 
- **Scalable**: 
- **Performance**: 
- **3-in-one**: 

## Examples
How to write a simple Http Stub? 
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


- mock-testing
- performance-testing
- auto-testing


## How to build

You require the following to build AK47:

* Latest stable [Oracle JDK 7](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Maven](http://maven.apache.org/)

Note that this is build-time requirement.  JDK 6 is enough to run your AK47-based application.



## Contributing 

Ak47 is free software/open source, and is distributed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Please read [Using pull requests](https://help.github.com/articles/using-pull-requests/), and feel free to contribute code or documentation.


## Questions?

You can try [creating an issue](https://help.github.com/articles/creating-an-issue/), or 
[searching issues](https://help.github.com/articles/searching-issues/), Or contact to [hannyu](https://github.com/hannyu).

