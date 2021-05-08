package liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo;

import java.util.HashSet;
import java.util.Set;

/**
 * 模拟数据库中查出的授权信息
 *
 * @author liuyang
 * @scine 2021/4/8
 */
public class UserService {

    public User queryByName(String name) {

        User user = new User();
        user.setUseranme("liuyang");
        user.setPassword("123456");

        Set<String> permissions = new HashSet<>();
        permissions.add("managerxxx");
        permissions.add("user:delete");
        permissions.add("user:update");
        user.setPermissions(permissions);

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        user.setRoles(roles);

        return user;
    }
}
