package liuyang.testspringbootenvshiro.modules.security.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import liuyang.testspringbootenvshiro.modules.security.shiro.dao.mbp.FilterChainDefinitionMapBuilder;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.UserRealm;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo.User;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo.UserHelloRealm;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo02.UserHelloRealm02;
import liuyang.testspringbootenvshiro.modules.security.shiro.session.RedisSessionManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyang
 * @scine 2021/4/1
 *
 * 注册三大件：
 * 1. Realm
 * 2. DefaultWebSecurityManager
 * 3. ShiroFilterFactoryBean
 *
 * 联想Shiro核心组件：
 * 1. Subject <-- the current 'user'
 * 2. SecurityManger <-- manages all Subjects
 * 3. Realm <-- access your security data
 */
@Configuration
public class ShiroConfig {

    @Autowired
    FilterChainDefinitionMapBuilder filterChainDefinitionMapBuilder;

    /*
    在Web程序中，Shiro进行权限控制是通过一组过滤器完成的。
     */
    // ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 编写认证授权规则（过滤器集合）
        // 有两种方式支持鉴权
        // 1.配置过滤器。
        // 2.使用注解。 @RequiresPermissions() @RequiresRoles()
        // http://shiro.apache.org/web.html#Web-defaultfilters
        // 视频：https://www.bilibili.com/video/BV1QJ411S7c4?p=122
        /**
         * anon: 无需认证。无参，开放权限，可理解为匿名或者游客。
         * authc: 必须认证。无参，需要认证。
         * perms: 必须拥有某个权限才能访问。参数可写多个。e.g. perms[ user, admin]
         * roles: 必须拥有某个角色才能访问。参数可写多个。e.g. roles[admin, user]
         *
         * user：不一定通过认证，只要曾经被shiro记录即可，比如：记住我
         * port: 限定访问端口。
         * rest: 请求必须是RESTful的。
         * ssl: 必须是安全的URL请求，协议必须是HTTPS。
         */
        // Map<String, String> filterChainDefinitionMap = new HashMap<>();
        /*
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();// 问：为啥用LinkedHashMap? 一个回答：在用xml配置的时候，框架默认就是用的LinkedHashMap。又答：LinkedHashMap可以保证顺序。
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 配置规则
        // 如何将这一部分放入数据库：https://www.bilibili.com/video/BV1YW411M7S3?p=22
        // 答：写个Service，返回一个LinkedHashMap对象，赋给shiroFilterFactoryBean即可。
        // 视频演示了一种实例工厂方法的办法。可以参考。
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/main", "authc");             // 访问/main必须登录
        filterChainDefinitionMap.put("/manager", "perms[managerxxx]"); // 访问/manager必须具有manager权限
        filterChainDefinitionMap.put("/admin", "roles[admin]");     // 访问/admin必须具有admin角色
        filterChainDefinitionMap.put("/**", "authc");               // 这个放在最后
        */
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMapBuilder.build());


        // 若使用JWT 则进行如下配置

        // 若是使用页面方式 则进行如下配置
        shiroFilterFactoryBean.setLoginUrl("/login"); // 默认会找/login.jsp
        shiroFilterFactoryBean.setUnauthorizedUrl("/401"); // 设置未授权的页面

        return shiroFilterFactoryBean;
    }

    // DefaultWebSecurityManager
    // 1. 配Realm 以及 认证、授权的策略
    // 2. 配缓存
    // 3. 配会话管理
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm
            , @Qualifier("iniRealm") IniRealm iniRealm
            , @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager
            , @Qualifier("redisSessionManager")SessionManager redisSessionManager
            , @Qualifier("modularRealmAuthenticator") ModularRealmAuthenticator modularRealmAuthenticator) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 1. 配置Realm或者Authenticator（多Realm场景）
        // 1.1 单Realm
        // defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRealm(iniRealm);// 测试使用, 从shiro_user.ini文件中读取
        // 1.2.1 多Realm 将Realm配置给Authenticator
        // defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator);
        // 1.2.2 多Realm 将Realm配置给SecurityManager的Realms (推荐)
        /*
        List<Realm> realms = new ArrayList<>();
        realms.add(userHelloRealm);
        realms.add(userHelloRealm02);
        defaultWebSecurityManager.setRealms(realms);
        defaultWebSecurityManager.setAuthenticator(new ModularRealmAuthenticator());// 使用默认认证策略 AtLeastOneSuccessfulStrategy
        // defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator); //订制策略
        */

        // 2. 配置缓存管理器（？？不手动配置会生效么？ Redis的）
        // 默认情况缓存使用的是：？？
        defaultWebSecurityManager.setCacheManager(redisCacheManager);

        // 3. 配置会话管理器
        // 默认情况是使用的：ServletContainerSessionManager。如果使用Redis存储session，则应做配置
        defaultWebSecurityManager.setSessionManager(redisSessionManager);

        //

        return defaultWebSecurityManager;
    }

    // 创建 Realm 对象
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public IniRealm iniRealm() {
        return new IniRealm("classpath:shiro_user.ini");
    }

    @Bean
    public UserHelloRealm userHelloRealm() {
        UserHelloRealm userHelloRealm = new UserHelloRealm();

        // 散列规则
        // 这里指定的hash规则应该与注册时使用的hash规则一致
        // 体现在 SimpleHash(String algorithmName, Object source, Object salt, int hashIterations) 中的algorithmName和hashIterations
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// algorithmName
        // hashedCredentialsMatcher.setHashIterations(5);// hashIterations 默认-1

        userHelloRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        return userHelloRealm;
    }

    @Bean
    public UserHelloRealm02 userHelloRealm02() {
        UserHelloRealm02 userHelloRealm02 = new UserHelloRealm02();

        // 散列规则 TODO


        return userHelloRealm02;
    }

    // 如果涉及到多个Realm认证(貌似Shiro还支持多个Realm授权)
    // Demo 推荐配置方式参见public DefaultWebSecurityManager defaultWebSecurityManager(...)
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(UserHelloRealm userHelloRealm, UserHelloRealm userHelloRealm02) {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();

        // 1. Realms （Realms推荐配置在SecurityManager中的写法，整个这个@Bean都不需要！）
        List<Realm> realms = new ArrayList<>();
        realms.add(userHelloRealm);
        realms.add(userHelloRealm02);

        // 2. 认证策略 AuthenticationStrategy
        // ModularRealmAuthenticator默认使用AtLeastOneSuccessfulStrategy
        // modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy()); // default
        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        // modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());

        modularRealmAuthenticator.setRealms(realms);
        return modularRealmAuthenticator;
    }

    // 整合thymeleaf-extras-shiro
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    // //////////////////////////////////////////////////////////////////////////
    // 配置支持shiro注解
    // pdt中配了这个，但是renren-security中没有
    // 不过好像这个默认值就是true。？？在IntelliJ IDEA中如何查看某一个类的属性的默认值？或者说运行时的值？
    /*
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
     */

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager);
        return advisor;
    }

    // //////////////////////////////////////////////////////////////////////////
    // Redis
    // 注意1: 除此之外需要先指定sesssionId的获取方式，参见RedisSessionManager)
    // 注意2；在Redis接管session之后，获取session的方法变为subject.getSession().
    // 1. Redis的控制器，操作Redis
    // 2. sessionDao (pdt没配 应该是与使用JWT有关)
    // 3. 会话管理器 (pdt没配 应该是与使用JWT有关)
    // 4. 缓存管理器 (pdt的没有配置@Bean)

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private int REDIS_PORT;

    // 1. Redis的控制器，操作Redis
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(REDIS_HOST);
        redisManager.setPort(REDIS_PORT);
        return redisManager;
    }

    // 2. RedisSessionDAO
    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    // 3. 会话管理器 (RedisSessionManager是指定sessionId获取方式的。自定义。)
    @Bean
    public DefaultWebSessionManager redisSessionManager(RedisSessionDAO redisSessionDAO) {
        RedisSessionManager redisSessionManager = new RedisSessionManager();// 这东西是用户自定义的。本示例是一种写法，完全可以把订制过程就写在这个配置类中。
        redisSessionManager.setSessionDAO(redisSessionDAO);
        return redisSessionManager;
    }

    // 4. 缓存管理器
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

}
