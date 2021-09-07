/**
 * 1. 所有的配置工作在ShiroConfig.java中完成。
 * 2. 依赖：需要先在pom.xml中引入这个ehcache 依赖 shiro-ehcache 依赖.详见pom.xml
 * 3. 关于序列化：貌似使用Ehcache是不需要额外的序列化与反序列化动作的。
 *
 * @author liuyang
 * @scine 2021/9/6
 */
package liuyang.testspringbootenvshiro.modules.security.shiro.cache.ehcache;


    // //////////////////////////////////////////////////////////////////////////
    // Ehcache 20210907 add
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
    /*
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
    */