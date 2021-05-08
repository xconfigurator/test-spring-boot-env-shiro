package liuyang.testspringbootenvshiro.modules.security.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 订制 使用Redis来管理Session
 * @author liuyang
 * @scine 2021/4/8
 */
public class RedisSessionManager extends DefaultWebSessionManager {

    // 指定session id的获取方式
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // return super.getSessionId(request, response);

        // 获取请求头中Authorization中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)) {
            // 如果没有携带， 生成新的sessionId。
            return super.getSessionId(request, response);
        } else {
            // 返回sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }
}
