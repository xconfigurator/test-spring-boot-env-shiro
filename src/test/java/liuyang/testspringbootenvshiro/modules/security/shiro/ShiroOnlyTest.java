package liuyang.testspringbootenvshiro.modules.security.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 一个脱离Spring Boot容器只使用shiro的场景
 * @author liuyang
 * @scine 2021/4/8
 */
@Slf4j
public class ShiroOnlyTest {

    // private static final String INI_FILE = "shiro_user.ini"; // shiro_user_hello_realm.ini
    private static final String INI_FILE = "shiro_user_hello_realm.ini";

    @BeforeEach
    public void beforeEach() {
        // 1. 根据配置文件创建SecurityManagerFactory
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:" + INI_FILE);
        // 2. 通过工厂获取SecurityManager
        SecurityManager securityManager = factory.getInstance();
        // 3. 将SecurityManager绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);
    }

    // 认证
    @Test
    public void testLogin() {
        // 1. 2. 3. @BeforeEach
        // 4. 从当前运行环境中构造subject
        Subject subject = SecurityUtils.getSubject();
        // 5. 构造shiro登录数据
        String username = "liuyang";
        String password = "123456";
        // String password = "1234567"; // apache.shiro.authc.IncorrectCredentialsException: Submitted credentials for token [org.apache.shiro.authc.UsernamePasswordToken - liuyang, rememberMe=false] did not match the expected credentials.
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 6. 主体登录
        subject.login(token);

        // 7. 从Subject中获取信息
        log.info("subject.isAuthenticated() = " + subject.isAuthenticated());
        log.info("subject.getPrincipal() = " + subject.getPrincipal());
    }

    // 授权
    @Test
    public void testAuthorization() {
        // 先登录
        Subject subject = SecurityUtils.getSubject();
        String username = "liuyang";
        String password = "123456";
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);

        // 来看授权信息
        Assertions.assertEquals(true, subject.hasRole("admin"));
        Assertions.assertEquals(true, subject.isPermitted("managerxxx"));
        Assertions.assertEquals(false, subject.hasRole("role1"));
        Assertions.assertEquals(false, subject.isPermitted("xxx"));
    }
}
