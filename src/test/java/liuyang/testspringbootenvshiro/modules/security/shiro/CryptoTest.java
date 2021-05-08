package liuyang.testspringbootenvshiro.modules.security.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * 测试Shiro提供的加解密方法。
 * 参考视频：
 * 散列算法     https://www.bilibili.com/video/BV1vE411i7ij?p=14
 * 散列工具类   https://www.bilibili.com/video/BV1vE411i7ij?p=15
 * 实现加密     https://www.bilibili.com/video/BV1vE411i7ij?p=16 （啰嗦）
 * 加盐        https://www.bilibili.com/video/BV1vE411i7ij?p=17 （加盐的一些讲究 用到的时候再看一遍。 公盐， 私盐。这个人逻辑比较混乱。）
 *
 * 参考文档：
 * http://shiro.apache.org/cryptography.html
 *
 * @author liuyang
 * @scine 2021/4/8
 */
@Slf4j
public class CryptoTest {

    private static final String PLAN_TEXT = "liuyang";
    private static final String SALT = "ThinkPad"; // 实际应用中，这个盐随机生成，存储在数据库中。

    @Test
    public void testUUID() {
        log.info(UUID.randomUUID().toString());
    }

    @Test
    public void testMd5Hash() {
        String str = new Md5Hash(PLAN_TEXT).toString();
        log.info(str);
    }

    @Test
    public void testMd5HashWithSalt() {
        String str = new Md5Hash(PLAN_TEXT, SALT).toString();
        log.info(str);
    }

    @Test
    public void testMd5HashWithSaltTwice() {// 两次md5
        String str = new Md5Hash(PLAN_TEXT, SALT, 2).toString();
        log.info(str);
    }

    // Shiro 内部使用的也是这个接口
    @Test
    public void testSimpleHash() {
        String str = new SimpleHash("MD5", PLAN_TEXT, SALT).toString();
        log.info(str);

        String strIter2 = new SimpleHash("MD5", PLAN_TEXT, SALT, 2).toString();// 迭代两次
        log.info(strIter2);
    }
}
