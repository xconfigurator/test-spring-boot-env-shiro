package liuyang.testspringbootenvshiro.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理页面
 * @author liuyang
 * @scine 2021/4/2
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationException.class)
    public String handleAuthorizationExcepton(AuthorizationException ae)  {
        // 注意，使用注解方式来控制权限时，需要在这里配置一下。
        log.error(ae.getMessage(), ae);
        return "未授权";
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error(e.getMessage(), e);
        return "Server ERROR (异常被ExceptionHandler处理)";
    }
}
