package liuyang.testspringbootenvshiro.modules.security.shiro.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class EncryptUtilForShiroTest {

    @Test
    void test() {
        log.info("enc password = {}", EncryptUtilForShiro.getMD5Str("123456", EncryptConst.DEFAULT_SALT, 2));
        log.info("enc password = {}", EncryptUtilForShiro.getMD5Str("123456"));
        log.info("end password = {}", EncryptUtilForShiro.getMD5Str("123456", null, EncryptConst.DEFAULT_ITERATIONS));
    }
}
