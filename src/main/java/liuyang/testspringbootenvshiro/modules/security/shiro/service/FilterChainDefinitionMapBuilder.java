package liuyang.testspringbootenvshiro.modules.security.shiro.service;

import java.util.LinkedHashMap;

/**
 * 实例工厂的方法 返回filterChainDefinitionMap
 * @author liuyang
 * @scine 2021/5/10
 * @Update 2021/8/28
 *         2022/8/12 根据YouTube视频 Apache shiro项目发起人视频更新笔记
 *
 * Authroization:
 * Process of determing Access Control "who can do what"
 * Elements of Authorization:
 * 1. Permissions
 * 2. Roles
 * 3. Users
 *
 * Permissions:
 * The "what" of an application
 * Most atomic security element
 * Describes resource types and their behavior
 * Does not define "who"
 *
 * Roles:
 * Implicity or Explicity construct
 * Implicit: Name only
 * Explicit: A named collection of Permissions
 *  Allows behavior aggregation
 *  Enables dynamic(runtime) alteration of user abilities.
 *
 *
 */
//@Component// Bean注入有问题啊！！
public class FilterChainDefinitionMapBuilder {

    // 把实现换为从数据库中查即可。
    public static LinkedHashMap<String, String> build() {

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();// 问：为啥用LinkedHashMap? 一个回答：在用xml配置的时候，框架默认就是用的LinkedHashMap。又答：LinkedHashMap可以保证顺序。

        // ///////////////////////////////////////////////////
        // 白名单 begin
        // 20220903 add
        filterChainDefinitionMap.put("/20220903/*", "anon");
        // 白名单 end
        // ///////////////////////////////////////////////////

        // shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 配置规则
        // 如何将这一部分放入数据库：https://www.bilibili.com/video/BV1YW411M7S3?p=22
        // 答：写个Service，返回一个LinkedHashMap对象，赋给shiroFilterFactoryBean即可。
        // 视频演示了一种实例工厂方法的办法。可以参考。
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/static/*", "anon");
        filterChainDefinitionMap.put("/main", "authc");             // 访问/main必须登录
        filterChainDefinitionMap.put("/manager", "perms[managerxxx]"); // 访问/manager必须具有manager权限 其他例子：perms[user:add]
        filterChainDefinitionMap.put("/admin", "roles[admin]");     // 访问/admin必须具有admin角色
        filterChainDefinitionMap.put("/**", "authc");               // 这个放在最后


        return filterChainDefinitionMap;
    }

}
