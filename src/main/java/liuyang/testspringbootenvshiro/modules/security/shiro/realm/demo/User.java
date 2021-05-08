package liuyang.testspringbootenvshiro.modules.security.shiro.realm.demo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author liuyang
 * @scine 2021/4/8
 *
 * 说明：
 * 如果安全对象需要保存到Redis管理的session之中去，则需要实现一个接口AuthCachePrincipal,不过貌似这个接口已经被标注为过时……
 *
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 8185244643946332814L;

    private String useranme;
    private String password;
    private String salt;
    private Set<String> roles;
    private Set<String> permissions;
}
