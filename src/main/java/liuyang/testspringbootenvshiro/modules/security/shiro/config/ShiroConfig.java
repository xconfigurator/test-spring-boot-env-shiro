package liuyang.testspringbootenvshiro.modules.security.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.UserRealm;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo.User;
import liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo.UserHelloRealm;
import liuyang.testspringbootenvshiro.modules.security.shiro.session.RedisSessionManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
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
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 配置规则
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/main", "authc");             // 访问/main必须登录
        filterChainDefinitionMap.put("/manager", "perms[managerxxx]"); // 访问/manager必须具有manager权限
        filterChainDefinitionMap.put("/admin", "roles[admin]");     // 访问/admin必须具有admin角色


        // 若使用JWT 则进行如下配置

        // 若是使用页面方式 则进行如下配置
        shiroFilterFactoryBean.setLoginUrl("/login"); // 默认会找/login.jsp
        shiroFilterFactoryBean.setUnauthorizedUrl("/401"); // 设置未授权的页面

        return shiroFilterFactoryBean;
    }

    // DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm
        , @Qualifier("iniRealm") IniRealm iniRealm
        , @Qualifier("redisSessionManager")SessionManager redisSessionManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRealm(iniRealm);// 测试使用, 从shiro_user.ini文件中读取

        // 配置会话管理器
        // 默认情况是使用的：ServletContainerSessionManager。如果使用Redis存储session，则应做配置
        defaultWebSecurityManager.setSessionManager(redisSessionManager);

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

        // 这里指定的hash规则应该与注册时使用的hash规则一致
        // 体现在 SimpleHash(String algorithmName, Object source, Object salt, int hashIterations) 中的algorithmName和hashIterations
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// algorithmName
        // hashedCredentialsMatcher.setHashIterations(5);// hashIterations 默认-1

        userHelloRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        return userHelloRealm;
    }

    // 整合thymeleaf-extras-shiro
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    // //////////////////////////////////////////////////////////////////////////
    // 配置支持shiro注解
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
