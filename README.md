# dubbo-demo

1.安装 zookeeper 并启动

2.依次启动 DubboServerApplication 和 DubboConsumerApplication 两个微服务

3.可以使用 dubbo-admin 来查看 dubbo 的使用情况,将 dubbo-admin.war 放入 tomcat 中启动即可，密码为 root/root guest/guest （该 war 由 dubbo-admin 2.5.10 版本编译）


## 重点 1

使用 @com.alibaba.dubbo.config.annotation.Reference 注入分布式的远程服务对象。

在消费者端，使用 Springmvc 的时候这里是使用 @Autowired 注入 Service。同样的，如果是使用 XML 配置文件的方式配置 dubbo 这里也是使用 @Autowired 注入 Service。当使用 dubbo 注解的方式注入 Service 的时候，可以使用 @Reference 注解注释需要注入的 Service。这个注解相当于:

```Java
<dubbo:reference id="demoServiceRemote" interface="com.alibaba.dubbo.demo.DemoService" /> 
```

但是使用该注解在 当前版本（dubbo-spring-boot-starter v2.0.0）中是没有办法添加 method 参数的。比如：

```Java
<dubbo:reference id="fooService" interface="com.alibaba.foo.FooService">
      <dubbo:method name="findFoo" async="true" />
</dubbo:reference>
```

像上面的配置文件中，指定 FooService 中 finFoo() 为异步方法，使用注解就无法做到。使用注解的方式只能够将当前 Service 全部的方法变成异步方法，而无法指定某一个方法。

```Java
@Reference(async = true)
private AsyncApiService asyncApiService;
```

## 重点 2

使用 @com.alibaba.dubbo.config.annotation.Service 注解。在生产者端，可以使用 @Service 注解注释 ServiceImpl，该注解相当于 XML 配置文件：

```Java
<dubbo:service interface="com.alibaba.dubbo.demo.DemoService" ref="demoServiceLocal" />  
<dubbo:reference id="demoServiceRemote" interface="com.alibaba.dubbo.demo.DemoService" /> 
```

Service 注解同样拥有很多的参数，在 com.alibaba.dubbo.config.annotation 包下的 @interface Service 中有全部的参数，比如常用的 version，token，timeout 等都是支持的。

```Java
@Service(interfaceClass = ApiService.class, timeout = 5000)
```

interfaceClass 可以指定接口类，也可以不写，会默认使用当前类实现的接口。

## 重点 3 只注册、直连提供者

在 application.properties 中指定 spring.dubbo.registry.register=false，可以使得生产者不注册到注册中心，和直连提供者配合做本地调试。

直连提供者可以使得消费者直接消费生产者，而不通过注册中心发现服务。直连提供者可以通过 XML 配置、通过 -D 参数指定以及通过文件映射。推荐使用通过文件映射的方式来实现直连提供者。

首先 dubbo 在 `2.0` 以上版本自动加载 ${user.home}/dubbo-resolve.properties 文件，我们只需要在 ${user.home} 中添加一个 dubbo-resolve.properties 文件（Mac:~/   Windows:C:\User\XXX）。

然后在映射文件 `xxx.properties` 中加入配置，其中 key 为服务名，value 为服务提供者 URL：

```Java
com.inshare.dubbo.common.service.ApiService=dubbo://localhost:20880
```

如果注册中心没有相应的生产者，就会到这里找生产者，如果这里也没有找到生产者，则报错。

注意：为了避免复杂化线上环境，不要在线上使用这个功能，只应在测试阶段使用。

## 重点 4 关闭启动时检查

Dubbo 缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止 Spring 初始化完成，以便上线时，能及早发现问题，默认 check="true"。但是有的时候，比如测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动等情况时，就必须让 dubbo 不进行启动时检查操作。只需要在配置文件中添加：

```Java
## 强制改变所有 reference 的 check 值，就算配置中有声明，也会被覆盖
spring.dubbo.reference.com.foo.BarService.check=false
## 关闭服务的启动时检查 (没有提供者时报错)
spring.dubbo.reference.check=false
## 关闭所有服务的启动时检查 (没有提供者时报错)
spring.dubbo.consumer.check=false
## 关闭注册中心启动时检查 (注册订阅失败时报错)
spring.dubbo.registry.check=false
```

## 重点 5 服务分组

当一个接口有多种实现时，可以用 group 区分。

生产者、消费者配置文件中添加：

```Java
spring.dubbo.registry.group=group1
```

XML 方式：

```Java
<!-- 服务 -->
<dubbo:service group="feedback" interface="com.xxx.IndexService" />
<dubbo:service group="member" interface="com.xxx.IndexService" />

<!-- 引用 -->
<dubbo:reference id="feedbackIndexService" group="feedback" interface="com.xxx.IndexService" />
<dubbo:reference id="memberIndexService" group="member" interface="com.xxx.IndexService" />

<!-- 任意组 -->
<dubbo:reference id="barService" interface="com.foo.BarService" group="*" />
```

如果使用 dubbo-admin 监测 dubbo，则需要在 `${TOMCAT_HOME}/webapps/ROOT/WEB-INF/dubbo.properties` 配置 `dubbo.registry.group=group`

`${TOMCAT_HOME}/webapps/ROOT/WEB-INF/classes/META-INF/spring/dubbo-admin.xml` 的 `<dubbo:registry />` 参数中添加 `group="${dubbo.registry.group}"`。

如果不配置 dubbo-admin 只能显示非 group 中的消费者和生产者。

## 重点 6 参数验证

参数验证功能是基于 [JSR303](https://jcp.org/en/jsr/detail?id=303) 实现的，用户只需标识 JSR303 标准的验证 annotation，并通过声明 filter 来实现验证。

maven 依赖：(如果是使用 springboot web start 则不需要引入)

```Java
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.0.0.GA</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>4.2.0.Final</version>
</dependency>
```

首先在消费者端使用注解，声明使用参数验证：

```Java
@Reference(validation = "true")
```

或者在生产者端声明使用参数验证：

```Java
@Service(interfaceClass = ValidationApiService.class, timeout = 5000, validation = "true")
```

分组验证例子：

```Java
// 缺省可按服务接口区分验证场景，如：@NotNull(groups = ValidationService.class)   
public interface ValidationService { 
    // 与方法同名接口，首字母大写，用于区分验证场景，如：@NotNull(groups = ValidationService.Save.class)，可选
    @interface Save{} 
    void save(ValidationParameter parameter);
    void update(ValidationParameter parameter);
}
```

关联验证例子：

```Java
import javax.validation.GroupSequence;
 
public interface ValidationService {
    // 同时验证Update组规则
    @GroupSequence(Update.class) 
    @interface Save{}
    void save(ValidationParameter parameter);
 
    @interface Update{} 
    void update(ValidationParameter parameter);
}
```

参数验证例子：

```Java
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
 
public interface ValidationService {
    // 验证参数不为空
    void save(@NotNull ValidationParameter parameter);
    // 直接对基本类型参数验证
    void delete(@Min(1) int id);
}
```

最后可以使用 `RpcException e` 捕获参数验证失败的异常

```Java
import com.alibaba.dubbo.examples.validation.api.ValidationParameter;
import com.alibaba.dubbo.examples.validation.api.ValidationService;
import com.alibaba.dubbo.rpc.RpcException;

catch (RpcException e) {
    // 里面嵌了一个ConstraintViolationException
	ConstraintViolationException ve = (ConstraintViolationException) e.getCause(); 
	// 可以拿到一个验证错误详细信息的集合
	Set<ConstraintViolation<?>> violations = ve.getConstraintViolations(); 
	System.out.println(violations);
}

```

## 重点 7 结果缓存

用于加速热门数据的访问速度。

dubbo 有三种缓存类型：

`lru` 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。

`threadlocal` 当前线程缓存，比如一个页面渲染，用到很多 portal，每个 portal 都要去查用户信息，通过线程缓存，可以减少这种多余访问。

`jcache` 与 [JSR107](http://jcp.org/en/jsr/detail?id=107%27) 集成，可以桥接各种缓存实现。

同样的这里使用 XML 配置可以指定到 method 颗粒度上，而使用注解的方式只能够达到 Service 颗粒度上。

```Java
<!-- Service -->
<dubbo:reference interface="com.foo.BarService" cache="lru" />

<!-- 指定方法 -->
<dubbo:reference interface="com.foo.BarService">
    <dubbo:method name="findBar" cache="lru" />
</dubbo:reference>

<!-- 使用注解的方式实现 -->
@Reference(cache = "lru")
```

## 重点 8 隐式传参

可以通过 `RpcContext` 上的 `setAttachment` 和 `getAttachment` 在服务消费方和提供方之间进行参数的隐式传递。

生产者 Service 中：

```Java
RpcContext.getContext().setAttachment("index", "1");
```

消费者 Service 中：

```Java
String index = RpcContext.getContext().getAttachment("index");
System.out.println("隐式传参 index = " + index);
```

隐式传参用于框架集成，不建议常规业务使用。

## 重点 9 异步调用

基于 NIO 的非阻塞实现并行调用，客户端不需要启动多线程即可完成并行调用多个远程服务，相对多线程开销较小。

在 consumer.xml 中配置：

```Java
<dubbo:reference id="fooService" interface="com.alibaba.foo.FooService">
      <dubbo:method name="findFoo" async="true" />
</dubbo:reference>
<dubbo:reference id="barService" interface="com.alibaba.bar.BarService">
      <dubbo:method name="findBar" async="true" />
</dubbo:reference>
```

或者使用注解：

```Java
@Reference(async = true)
```

同样的使用注解没有办法控制到 Method 级别，只能使得被注解的 Service 的全部方法都支持异步调用。

Controller 中可以使用多种方法实现异步调用:

```Java
// 正常调用，不使用 dubbo async
syncService.findFoo();
int bar = asyncService.findBar();
System.out.println("bar = " + bar);

// 异步调用，使用 dubbo async get()的时候直接拿到返回值，否则线程wait住，等待 bar 返回后，线程会被notify唤醒
asyncService.findFoo();
asyncService.findBar();
Future<Integer> barFuture = RpcContext.getContext().getFuture();
System.out.println("async bar = " + barFuture.get());

// 使用 JDK 8 CompletableFuture 的方式异步
CompletableFuture.runAsync(asyncService::findFoo);
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(asyncService::findBar);
System.out.println("async bar = " + future.get());
```

## 重点 10 springboot 使用 xml 配置文件配置 dubbo

在使用 springboot 集成 dubbo 的时候往往都是使用 properties 或者 yml 配置文件的方式配置 dubbo，而不像传统的 springmvc 项目使用 xml 的方式配置 dubbo，在使用注解方式配置 dubbo 的时候有的时候没有办法达到预期的效果，毕竟 dubbo-start 开发的完整度还不是很高。我们也可以手动配置 xml 配置文件让 springboot 使用 xml 配置  dubbo。

首先在项目 resources 文件下新建 dubbo-consume.xml（消费者同样):

```Java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 重点 10 springboot 使用 xml 配置文件配置 dubbo -->
    <!-- 必须在配置文件中重新配置 application 等参数，好像不和 properties 共用 -->

    <dubbo:application name="dubbo-consumer" />
    <dubbo:registry address="zookeeper://localhost:2181" />
    <dubbo:protocol name="dubbo" port="20889" />

    <!--接口位置-->
    <dubbo:reference id="apiService" interface="com.inshare.dubbo.common.service.ApiService" />

</beans>
```

这里要注意即使在 properties 中指定了 application、registry 等配置，这里还是需要再指定一下，否则启动会找不到配置。

使用 xml 方式注入方法之后，原本使用 @Reference 声明的 Service 就要修改成使用 @Autowired 注入。

最后在 springboot 启动方法上面添加：

```Java
import org.springframework.context.annotation.ImportResource;

// 指定配置文件
@ImportResource({"classpath:dubbo-consume.xml"})
```

这样就可以既使用注解又可以当注解不能满足业务需求的时候使用 xml 进行详细的配置。

## 重点 11 使用 java config 的方式配置 dubbo

dubbo 可以是用 java config 的形式进行配置，首先新建 DubboConfiguration.java
这里要注意，Spring 的配置文件使用 @Configuration 注解，并且要和 Springboot application 启动类在同一个目录下
并且需要将 @EnableDubboConfiguration 去除，转而使用 @DubboComponentScan 进行包扫描

```Java
@Configuration
public class DubboConfiguration {
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-provider");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://localhost:2181");
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(20880);
        protocolConfig.setName("dubbo");
        return protocolConfig;
    }
}
```

使用 java config 配置后就不需要使用 properties 指定端口什么的了。