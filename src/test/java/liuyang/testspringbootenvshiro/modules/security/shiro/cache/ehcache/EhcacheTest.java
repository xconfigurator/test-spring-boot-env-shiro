package liuyang.testspringbootenvshiro.modules.security.shiro.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Ehcache 2.x
 * https://www.ehcache.org/generated/2.10.4/html/ehc-all/#page/Ehcache_Documentation_Set%2Fco-codebasics_loading_a_configuration.html%23
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
