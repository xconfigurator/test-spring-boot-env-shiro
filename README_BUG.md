# 20210830 RedisSession问题
```java
// ShiroConfig.javas
// 临时处理
//defaultWebSecurityManager.setSessionManager(redisSessionManager);
```

```shell
# 错误堆栈
2021-08-30 08:32:25.372  INFO 45532 --- [p-nio-80-exec-1] a.s.s.m.AbstractValidatingSessionManager : Enabling session validation scheduler...
2021-08-30 08:32:28.752 ERROR 45532 --- [p-nio-80-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [java.lang.NullPointerException] with root cause

java.lang.NullPointerException: null
	at org.crazycake.shiro.RedisManager.init(RedisManager.java:27) ~[shiro-redis-3.1.0.jar:na]
	at org.crazycake.shiro.RedisManager.getJedis(RedisManager.java:41) ~[shiro-redis-3.1.0.jar:na]
	at org.crazycake.shiro.BaseRedisManager.set(BaseRedisManager.java:75) ~[shiro-redis-3.1.0.jar:na]
	at org.crazycake.shiro.RedisSessionDAO.saveSession(RedisSessionDAO.java:73) ~[shiro-redis-3.1.0.jar:na]
	at org.crazycake.shiro.RedisSessionDAO.doCreate(RedisSessionDAO.java:124) ~[shiro-redis-3.1.0.jar:na]
	at org.apache.shiro.session.mgt.eis.AbstractSessionDAO.create(AbstractSessionDAO.java:116) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.session.mgt.DefaultSessionManager.create(DefaultSessionManager.java:177) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.session.mgt.DefaultSessionManager.doCreateSession(DefaultSessionManager.java:158) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.session.mgt.AbstractValidatingSessionManager.createSession(AbstractValidatingSessionManager.java:136) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.session.mgt.AbstractNativeSessionManager.start(AbstractNativeSessionManager.java:99) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.mgt.SessionsSecurityManager.start(SessionsSecurityManager.java:152) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.subject.support.DelegatingSubject.getSession(DelegatingSubject.java:340) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.subject.support.DelegatingSubject.getSession(DelegatingSubject.java:316) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.util.WebUtils.saveRequest(WebUtils.java:607) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.AccessControlFilter.saveRequest(AccessControlFilter.java:208) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.AccessControlFilter.saveRequestAndRedirectToLogin(AccessControlFilter.java:191) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.authc.FormAuthenticationFilter.onAccessDenied(FormAuthenticationFilter.java:168) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.AccessControlFilter.onAccessDenied(AccessControlFilter.java:133) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.AccessControlFilter.onPreHandle(AccessControlFilter.java:162) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.PathMatchingFilter.isFilterChainContinued(PathMatchingFilter.java:203) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.filter.PathMatchingFilter.preHandle(PathMatchingFilter.java:178) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:131) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.AbstractShiroFilter$1.call(AbstractShiroFilter.java:365) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387) ~[shiro-core-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125) ~[shiro-web-1.4.0.jar:1.4.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1707) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at java.base/java.lang.Thread.run(Thread.java:834) ~[na:na]


```