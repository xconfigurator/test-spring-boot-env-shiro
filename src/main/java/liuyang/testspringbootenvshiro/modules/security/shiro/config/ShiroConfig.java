package liuyang.testspringbootenvshiro.modules.security.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo00.UserRealm;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo01.UserHelloRealm01;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo02.UserHelloRealm02;
import liuyang.testspringbootenvshiro.modules.security.shiro.session.RedisSessionManager;
import liuyang.testspringbootenvshiro.modules.security.shiro.util.EncryptConst;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyang
 * @scine 2021/4/1
 *
 * 参考视频：
 * https://www.bilibili.com/video/BV1ct411x7CN?p=5&spm_id_from=pageDriver&vd_source=8bd7b24b38e3e12c558d839b352b32f4
 *
 * 注册三大件：
 * 1. Realm
 * 2. DefaultWebSecurityManager
 * 3. ShiroFilterFactoryBean
 *
 * 注册Redis相关四大件
 * 1. RedisManager
 * 2. Shiro Session Manager -> RedisSessionManager
 * 3. Shiro Session DAO -> RedisSessionDAO
 * 4. Shiro Cache Manager -> RedisCacheManager
 *
 * 联想Shiro核心组件：
 * 1. Subject <-- the current 'user'
 * 2. SecurityManger <-- manages all Subjects
 * 3. Realm <-- access your security data
 *
 *  update 20220817
 *  How to Authenticate with Shiro
 *  1. Collect principals & credentials
 *  2. Submit to Authentication System
 *  3. Allow, retry, or block access
 *
 */
@ConditionalOnProperty(prefix = "liuyang", name = "security.shiro.enabled", havingValue = "true")
@Configuration
@Slf4j
public class ShiroConfig {

    /*
    @Autowired
    FilterChainDefinitionMapBuilder filterChainDefinitionMapBuilder;
    */

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ShiroFilterFactoryBean
    /*
    在Web程序中，Shiro进行权限控制是通过一组过滤器完成的。
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // //////////////////////////////////////////////////////////////////////////////
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // //////////////////////////////////////////////////////////////////////////////
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
        filterChainDefinitionMap.put("/manager", "perms[managerxxx]"); // 访问/manager必须具有manager权限 其他例子：perms[user:add]
        filterChainDefinitionMap.put("/admin", "roles[admin]");     // 访问/admin必须具有admin角色
        filterChainDefinitionMap.put("/**", "authc");               // 这个放在最后
        */
        shiroFilterFactoryBean.setFilterChainDefinitionMap(FilterChainDefinitionMapBuilder.build());// URL 拦截器参考”另外的方法“

        // //////////////////////////////////////////////////////////////////////////////
        // 未认证配置 若是使用页面方式 则进行如下配置
        shiroFilterFactoryBean.setLoginUrl("/login"); // 默认会找/login.jsp
        shiroFilterFactoryBean.setUnauthorizedUrl("/401"); // 设置未授权的页面

        // //////////////////////////////////////////////////////////////////////////////
        // 若使用JWT 则进行如下配置


        return shiroFilterFactoryBean;
    }

    // 另外的方法
    /*
    @Bean
    public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //definition.addPathDefinition("/login", "anon");
        //definition.addPathDefinitions(filterChainDefinitionMapBuilder.build());//null pointer?
        definition.addPathDefinitions(FilterChainDefinitionMapBuilder.build());
        return definition;
    }
     */

    // ////////////////////////////////////////////////////////////////////////////////////////
    // SecurityManager - DefaultWebSecurityManager
    // 1. 配Realm 以及 多Realm情况下的认证、授权的策略
    // 2. 配缓存
    // 3. 配会话管理
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(
            @Qualifier("iniRealm") IniRealm iniRealm
            , @Qualifier("userRealm") UserRealm userRealm
            , @Qualifier("userHelloRealm01") UserHelloRealm01 userHelloRealm01
            , @Qualifier("userHelloRealm02") UserHelloRealm02 userHelloRealm02
            //, @Qualifier("modularRealmAuthenticator") ModularRealmAuthenticator modularRealmAuthenticator
            , @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager
            , @Qualifier("redisSessionManager")SessionManager redisSessionManager
            , @Qualifier("ehCacheManager") EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // //////////////////////////////////////////////////////////////
        // 1. 配置Realm或者Authenticator（多Realm场景）
        // 1.1 单Realm
        // defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRealm(iniRealm);// 测试使用, 从shiro_user.ini文件中读取
        // 1.2.1 多Realm 将Realm配置给Authenticator
        // defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator);
        // 1.2.2 多Realm 将Realm配置给SecurityManager的Realms (推荐)
        /*
        // 位置不能搞错！ 20210829 ModularRealmAuthenticator设置需要放在设置多realm的前面。
        defaultWebSecurityManager.setAuthenticator(new ModularRealmAuthenticator());// 使用默认认证策略 AtLeastOneSuccessfulStrategy
        // defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator); //订制策略
        List<Realm> realms = new ArrayList<>();
        realms.add(userHelloRealm);
        realms.add(userHelloRealm02);
        defaultWebSecurityManager.setRealms(realms);
        */


        // //////////////////////////////////////////////////////////////
        // 2. 配置会话管理器
        // 说明：配置即生效，session操作，之前怎么用还怎么用。
        //      配置上Shiro之后，Shiro就会使用session管理器代理Tomcat（容器）管理的session等对象（通过Filter）
        //      视频说明：https://www.bilibili.com/video/BV1Up4y1s7MW?p=25
        // 2.1 关闭默认缓存
        // 关闭shiro自带的web的session。详见文档
        // http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications
        // 如果不关闭，则会使用Web Session。
        /*
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        */

        // 2.2 使用redisSessionManager
        // 默认情况是使用的：ServletContainerSessionManager。如果使用Redis存储session，则应做配置
        //defaultWebSecurityManager.setSessionManager(redisSessionManager);


        // //////////////////////////////////////////////////////////////
        // 3. 配置缓存管理器（？？不手动配置会生效么？ Redis的）
        // 缓存管理器配上就生效，不需要改变Realm中授权部分的写法，所以放心地在Realm授权部分查数据库。问题：认证是否也缓存？
        // 默认情况缓存使用的是：？？
        //defaultWebSecurityManager.setCacheManager(redisCacheManager);
        defaultWebSecurityManager.setCacheManager(ehCacheManager);


        // //////////////////////////////////////////////////////////////
        // 4. 其他功能，如rememberMe


        return defaultWebSecurityManager;
    }


    // ////////////////////////////////////////////////////////////////////////////////////////
    // Realm
    // 创建 Realm 对象
    @Bean
    public IniRealm iniRealm() {
        //return new IniRealm("classpath:shiro_user.ini");
        IniRealm iniRealm = new IniRealm("classpath:shiro_user.ini");

        // 散列规则(需要与注册（入库）时使用的加密策略保持一致。)
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(EncryptConst.DEFAULT_HASH_ALG_NAME);
        hashedCredentialsMatcher.setHashIterations(EncryptConst.DEFAULT_ITERATIONS);

        iniRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        return iniRealm;
    }

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public UserHelloRealm01 userHelloRealm01() {
        UserHelloRealm01 userHelloRealm01 = new UserHelloRealm01();

        // 散列规则
        // 这里指定的hash规则应该与注册时使用的hash规则一致
        // 体现在 SimpleHash(String algorithmName, Object source, Object salt, int hashIterations) 中的algorithmName和hashIterations
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// algorithmName
        // hashedCredentialsMatcher.setHashIterations(5);// hashIterations 默认-1

        userHelloRealm01.setCredentialsMatcher(hashedCredentialsMatcher);

        return userHelloRealm01;
    }

    @Bean
    public UserHelloRealm02 userHelloRealm02() {
        UserHelloRealm02 userHelloRealm02 = new UserHelloRealm02();

        // TODO 散列规则

        return userHelloRealm02;
    }

    // 如果涉及到多个Realm认证(貌似Shiro还支持多个Realm授权)
    // Demo 推荐配置方式参见public DefaultWebSecurityManager defaultWebSecurityManager(...)
    //@Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(UserHelloRealm01 userHelloRealm01, UserHelloRealm01 userHelloRealm02) {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();

        // 1. Realms （Realms推荐配置在SecurityManager中的写法，整个这个@Bean都不需要！）
        List<Realm> realms = new ArrayList<>();
        realms.add(userHelloRealm01);
        realms.add(userHelloRealm02);

        // 2. 认证策略 AuthenticationStrategy
        // ModularRealmAuthenticator默认使用AtLeastOneSuccessfulStrategy
        // modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy()); // default
        //
        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        // modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());

        modularRealmAuthenticator.setRealms(realms);
        return modularRealmAuthenticator;
    }

    // //////////////////////////////////////////////////////////////////////////
    // 配置支持shiro注解
    // pdt中配了这个，但是renren-security中没有
    // 不过好像这个默认值就是true。？？在IntelliJ IDEA中如何查看某一个类的属性的默认值？或者说运行时的值？
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

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
    // Ehcache 20210907 add (我嘞个去，再看到已经是一年前了。202209040028)
    // 视频：Shiro整合Ehcache https://www.bilibili.com/video/BV1Up4y1s7MW?p=22&spm_id_from=pageDriver
    // 前置条件：需要先在pom.xml中引入这个ehcache 依赖 shiro-ehcache 依赖
    // 注意：如果在容器中注入了本Bean，则需要取消RedisCacheManager的注入, 或者使用@Primary，否则：
    /*
    ***************************
    APPLICATION FAILED TO START
    ***************************

    Description:

    Field cacheManager in org.apache.shiro.spring.config.AbstractShiroConfiguration required a single bean, but 2 were found:
        - ehCacheManager: defined by method 'ehCacheManager' in class path resource [liuyang/testspringbootenvshiro/modules/security/shiro/config/ShiroConfig.class]
        - redisCacheManager: defined by method 'redisCacheManager' in class path resource [liuyang/testspringbootenvshiro/modules/security/shiro/config/ShiroConfig.class]

    Action:

    Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
    */
    @Primary // 又解锁Spring Framework的一个新功能
    @Bean
    public EhCacheManager ehCacheManager() {
        // 注意：这个EhCacheManager是在shrio-ehcache依赖中
        EhCacheManager ehCacheManager = null;
        InputStream inputStreamForPath = null;
        try {
            ehCacheManager = new EhCacheManager();
            // 注意：这个ResourceUtils是Shiro的工具类，而不是Spring的工具类
            inputStreamForPath = ResourceUtils.getInputStreamForPath("classpath:ehcache.xml");
            // 注意：这个CacheManager是Ehcache的。net.sf.ehcache.CacheManager
            CacheManager cacheManager = new CacheManager(inputStreamForPath);
            ehCacheManager.setCacheManager(cacheManager);
            return ehCacheManager;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (inputStreamForPath != null) {
                try {
                    inputStreamForPath.close();
                } catch (IOException e) {
                    log.error("读ehcache.xml的输入流未成功释放！");
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // CacheManager         org.apache.shiro.cache.CacheManager
    // WebSessionManager    org.apache.shiro.web.session.mgt.WebSessionManager
    //  Redis
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

    private String REDIS_CACHE_KEY = "shiro:cache";

    private String REDIS_SESSION_KEY = "shiro:session";

    private String COOKIE_NAME = "custom.name"; // TODO

    private String COOKIE_VALUE = "/"; // TODO

    // 1. Redis的控制器，操作Redis
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(REDIS_HOST);
        redisManager.setPort(REDIS_PORT);
        return redisManager;
    }

    // 关于Shiro Session的视频
    // https://www.bilibili.com/video/BV11e4y1n7BH?p=27&spm_id_from=pageDriver&vd_source=8bd7b24b38e3e12c558d839b352b32f4
    // 分类：
    // DefaultSessionManager: 用于Java SE环境
    // ServletContainerSessionManager: 用于web环境，直接使用Servlet容器的会话
    // DefaultWebSessionManager: 用于web环境，自己维护会话(不适用Servlet容器的会话管理)
    // 使用：
    // Session session = SecurityUtils.getSubject().getSession();
    // session.setAttribute("key", "value");
    // 说明：
    // Controller中的request，在shiro过滤器中的doFilterInternal方法，被包装秤ShiroHttpServletRequest
    // SecurityManager和SessionManager决定session来源于ServletRequest还是由Shiro管理的会话。
    // 无论是通过request.getSession或者subject。getSession获取到的session，操作session，两者等价。

    // 2. RedisSessionDAO
    // pdt-nms并没有配置
    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        //redisSessionDAO.setRedisManager("");
        return redisSessionDAO;
    }

    // 3. 会话管理器 (RedisSessionManager是指定sessionId获取方式的。自定义。)
    // pdt-nms并没有配置
    @Bean
    public DefaultWebSessionManager redisSessionManager(RedisSessionDAO redisSessionDAO) {
        RedisSessionManager redisSessionManager = new RedisSessionManager();// 这东西是用户自定义的。本示例是一种写法，完全可以把订制过程就写在这个配置类中。
        redisSessionManager.setSessionDAO(redisSessionDAO);

        // Cookie相关 begin
        redisSessionManager.setSessionIdCookieEnabled(true);// 允许支持Cookie
        redisSessionManager.setSessionIdUrlRewritingEnabled(true);// 允许支持请求的url重写（通过url来传递会话ID）
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName(COOKIE_NAME);
        cookie.setValue(COOKIE_VALUE);
        redisSessionManager.setSessionIdCookie(cookie);
        // Cookie相关 end

        return redisSessionManager;
    }

    // 4. 缓存管理器
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        // from pdt-nms
        //redisCacheManager.setPrincipalIdFieldName("id");// REDIS_CACHE_KEY
        // from pdt-nms
        //redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }


    // ////////////////////////////////////////////////////////////////////////////////////////
    // 整合thymeleaf-extras-shiro
    // 使用方法参考：https://blog.csdn.net/q15102780705/article/details/107445247
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }
}
