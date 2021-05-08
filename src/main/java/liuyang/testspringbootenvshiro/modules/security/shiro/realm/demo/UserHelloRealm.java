package liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * 一个最简单的不查数据库的认证授权Realm
 *
 * @author liuyang
 * @scine 2021/4/8
 */
@Slf4j
public class UserHelloRealm extends AuthorizingRealm {

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("执行了授权 doGetAuthorizationInfo(PrincipalCollection principals)");
        // 第三个人::黑马
        // 1. 获取登录相关信息（从认证方法保存的安全数据中获取）n
        User user = (User) principals.getPrimaryPrincipal();
        // 2. 根据id或者名称去查询用户(from principals)
        // 3. 查询用户的角色和权限(from principals)
        Set<String> permissions = user.getPermissions();
        Set<String> roles = user.getRoles();
        // 4. 构造返回值 (SimpleAuthorizationInfo)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions); // ? 疑问点：用add还是set？
        simpleAuthorizationInfo.addRoles(roles);// ? 疑问点：用add还是set？

        return simpleAuthorizationInfo;
    }

    // 认证
    // http://shiro.apache.org/authentication.html
    // 这部分佟刚讲得清晰！
    // 认证流程：https://www.bilibili.com/video/BV1YW411M7S3?p=8
    // Realm实现：https://www.bilibili.com/video/BV1YW411M7S3?p=9
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("执行了认证 doGetAuthenticationInfo(AuthenticationToken token)");
        // 1. 构造UsernamePasswordToken（转型）
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;// 来自客户端

        // 2. 获取输入的用户名和密码
        String username = usernamePasswordToken.getUsername() == null ? "" : usernamePasswordToken.getUsername();
        String password = String.valueOf(usernamePasswordToken.getPassword()) == null ? "" : String.valueOf(usernamePasswordToken.getPassword()); // 注意要处理一下

        // 3. 根据用户名和密码查询数据库（UserService）
        User user = new UserService().queryByName("liuyang");

        // 4. 根据数据库信息识别各种异常。
        // 4.1. 识别异常：如果失败：return null；或者抛出异常。
        if (null == user) {
            throw new UnknownAccountException("用户不存在");
        }
        // 4.2 ... 当然代码还可以完善，以便识别诸如用户是否已经被锁定等异常。

        // 5. 如果通过了异常检测，构建AuthenticationInfo类型的对象，向shiro存入安全数据。(SimpleAuthenticationInfo)
        // 密码对比：    https://www.bilibili.com/video/BV1YW411M7S3?p=10
        //              usernamePasswordToken.getPassword() (AuthenticationToken getCredentials()) 与
        //              SimpleAuthenticationInfo的getCredentials() (AuthenticationInfo getCredentials())方法 的返回值进行对比。
        //              实际跟踪代码可知：执行对比的是SimpleCredentialsMather 的 doCredentialsMatch(AuthenticationToken token， AuthenticationInfo info)方法
        //              上一级调用入口是AuthenticationRealm的getCredentialsMather来进行。
        // MD5:        https://www.bilibili.com/video/BV1YW411M7S3?p=11
        //              一个核心的接口CredentialsMather，Ctrl+H一下有惊喜。
        // 盐：         https://www.bilibili.com/video/BV1YW411M7S3?p=12
        // return new SimpleAuthenticationInfo(user, user.getPassword(), getName());// 明文
        // SimpleAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName)
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());// 密文加盐
    }
}
