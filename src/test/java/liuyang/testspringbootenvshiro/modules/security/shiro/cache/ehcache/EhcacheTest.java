package liuyang.testspringbootenvshiro.modules.security.shiro.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Ehcache 2.x
 * https://www.ehcache.org/generated/2.10.4/html/ehc-all/#page/Ehcache_Documentation_Set%2Fco-codebasics_loading_a_configuration.html%23
 * 视频：
 * EhCache API使用
 * https://www.bilibili.com/video/BV11e4y1n7BH?p=25&spm_id_from=pageDriver&vd_source=8bd7b24b38e3e12c558d839b352b32f4
 * Shiro整合EhCache
 * https://www.bilibili.com/video/BV11e4y1n7BH?p=26&spm_id_from=pageDriver&vd_source=8bd7b24b38e3e12c558d839b352b32f4
 *
 * @author liuyang
 * @scine 2021/9/6
 */
//@SpringBootTest
@Slf4j
public class EhcacheTest {

    @Test
    void helloEhcache() throws FileNotFoundException {
        URL resource = this.getClass().getClassLoader().getResource("ehcache.xml");
        CacheManager cacheManager = CacheManager.newInstance(resource);

        Cache cache = cacheManager.getCache("sampleCache2");
        Element element = new Element("foo", "bar");

        // 存
        cache.put(element);
        // 取
        Element foo = cache.get("foo");
        String valFromCache = (String) foo.getObjectValue();
        log.info("###########################################");
        log.info("foo = {}", valFromCache);
        log.info("###########################################");

        // 关闭
        cacheManager.shutdown();
    }
}
