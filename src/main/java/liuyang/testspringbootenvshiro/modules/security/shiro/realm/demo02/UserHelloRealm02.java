package liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo02;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 演示多Realm
 * 场景：
 *  UserHelloRealm用于验证存存放在MySQL中的用户数据（加密方式采用MD5，5次迭代，随机盐值...)
 *  UserHelloRealm02用于验证存放在DB2中的用户数据（加密方式采用sha256，不迭代，随机盐值...）
 *  ...
 * @author liuyang
 * @scine 2021/5/10
 */
public class UserHelloRealm02 extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // TODO
        return null;
    }
}
