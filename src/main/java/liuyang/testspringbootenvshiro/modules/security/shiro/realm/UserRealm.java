package liuyang.testspringbootenvshiro.modules.security.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liuyang
 * @scine 2021/4/1
 *
 * 注：
 * Realm <-- access your security data
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    // 可选 覆盖setName(String name) 会在构建SimpleAuthenticationInfo对象中使用。

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("执行了授权 doGetAuthorizationInfo(PrincipalCollection principals)");
        // 第三个人::黑马
        // 1. 获取登录相关信息（从认证方法保存的安全数据中获取）
        // 2. 根据id或者名称去查询用户(from principals)
        // 3. 查询用户的角色和权限(from principals)
        // 4. 构造返回值 (SimpleAuthorizationInfo)

        // 1. 获取登录相关信息（从认证方法保存的安全数据中获取）
        // Type1:
        // Account account = (Account) principals.getPrimaryPrincipal(); // 需要认证方法中return new SimpleAuthenticationInfo(user, user.getPwd(), "");第一个参数的配合。
        // Type2:
        // Subject subject = SecurityUtils.getSubject(); // Subject <-- the current 'user'
        // Account account = (Account) subject.getPrincipal(); // 需要认证方法中return new SimpleAuthenticationInfo(user, user.getPwd(), "");第一个参数的配合。
        // Type3:下面这样干还需要再次去查数据库，不要这样！
        // String username = (String) principals.getPrimaryPrincipal();// 需要认证方法中return new SimpleAuthenticationInfo(username, user.getPwd(), "");第一个参数的配合。

        // 2. 根据id或者名称去查询用户(from principals)
        // 3. 查询用户的角色和权限(from principals)

        // 4. 构造返回值 (SimpleAuthorizationInfo)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 设置角色
        Set<String> roles = new HashSet<>();
        // roles.add(account.getRole());
        simpleAuthorizationInfo.addRoles(roles);

        /// 设置权限
        Set<String> permisions= new HashSet<>();
        // permisions.add(account.getPerms());
        simpleAuthorizationInfo.addStringPermissions(permisions);

        return simpleAuthorizationInfo;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("执行了认证 doGetAuthenticationInfo(AuthenticationToken token)");
        // 第三个人::黑马
        // 1. 构造UsernamePasswordToken（转型）
        // 2. 获取输入的用户名和密码
        // 3. 根据用户名和密码查询数据库（UserService）
        // 4. 比较密码和数据库中的密码是否一致。
        // 5. 如果成功：向shiro存入安全数据。(SimpleAuthenticationInfo)
        // 6. 如果失败：return null；或者抛出异常。

        // 1. 构造UsernamePasswordToken（转型）
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;// 来源1： 这些来自客户端
        // 2. 获取输入的用户名和密码
        String username = usernamePasswordToken.getUsername();
        String password = String.valueOf(usernamePasswordToken.getPassword());
        // 3. 根据用户名和密码查询数据库（UserService）
        // Account account = userService.queryByName(username);
        // 4. 比较密码和数据库中的密码是否一致。
        /*
        // 注：省略了"加密"(散列 + 加盐)步骤
        if (account != null && account.getPassword() != null && account.getPassword().equals(password)) {
            // 5. 如果成功：向shiro存入安全数据。(SimpleAuthenticationInfo)
            // param1: 安全数据
            // param2: 密码
            // param3: 当前Realm的域名称
            return new SimpleAuthenticationInfo("用户信息对象", "account.getPassword()", getName()); // Shiro自己去比对密码
            return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            // 6. 如果失败：return null；或者抛出异常。
            return null;
            事实上，应该在这里根据规则抛出各种不同的异常，以便在LoginController中进行捕获。
        }
         */

        // 前两个人 楠和狂说的步骤
        /*
        // Account account = TODO 自定义Service来查询用户信息 // 来源2： 这些来自数据库（或者其他可能的数据源）
        if ("account" != null) {
            return new SimpleAuthenticationInfo("用户信息对象", "account.getPassword()", getName()); // Shiro自己去比对密码
        }
         */
        // TODO 看狂神P5 08:40
        // TODO 看狂神P6

        // 链接数据库
        /*
        User user = userService.queryUserByName(usernamePasswordToken.getUsername())
        if (user == null) {// 没有这个人
            return null;// UnknownAccountException
        }
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");// 在这里不会出现密码
        // 当然也可以配置使用密码
         */


        return null; // return null 在Controller中调用subject.login(token);就会抛出异常。
    }
}
