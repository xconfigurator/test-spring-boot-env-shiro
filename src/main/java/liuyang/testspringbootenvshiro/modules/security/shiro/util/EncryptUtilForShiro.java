package liuyang.testspringbootenvshiro.modules.security.shiro.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 加密、加盐策略在新用户注册以及验证的时候应保持一致
 *
 * @author liuyang
 * @scine 2021/8/26
 */
public class EncryptUtilForShiro {

    /**
     * @param password          待加密密码
     * @param salt              盐值(传递null表示无盐值，具体的跟代码)
     * @param hashIterations    迭代次数（this.iterations = Math.max(1, hashIterations);）
     * @return
     */
    public static String getMD5Str(String password, String salt, int hashIterations) {
        //return new Md5Hash(password, salt, hashIterations).toHex();
        return new SimpleHash(EncryptConst.DEFAULT_HASH_ALG_NAME, password, salt, hashIterations).toHex();
    }

    // 统一的默认散列策略
    public static String getMD5Str(String password) {
        return getMD5Str(password, EncryptConst.DEFAULT_SALT, EncryptConst.DEFAULT_ITERATIONS);
    }
}
