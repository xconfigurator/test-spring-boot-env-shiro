package liuyang.testspringbootenvshiro.modules.security.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 调用Shrio的登录控制器实现
 *
 * @author liuyang
 * @scine 2021/4/2
 *
 * 可以查看 shio的源码包中的example/quickstart
 */
@Controller
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public ModelAndView doLogin(String username, String password, ModelAndView mav) {
        log.info("登录操作");

        Subject subject = SecurityUtils.getSubject();

        // 20210508 add begin
        if (subject.isAuthenticated()) {
            mav.setViewName("index");
            return mav;
        }
        // 20210508 add end

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            mav.setViewName("index"); // 登录成功 回到首页
        } catch (UnknownAccountException e) {// 用户名错误
            String errorInfo = "用户 " + token.getPrincipal() + " 不存在";
            log.error(errorInfo);
            mav.addObject("username_has_error", true);
            mav.addObject("username_error_info", errorInfo);
            mav.setViewName("login");
        } catch (IncorrectCredentialsException e) { // 密码错误
            String errorInfo = "用户" + token.getPrincipal() + "密码错误";
            log.error(errorInfo);
            mav.addObject("password_has_error", true);
            mav.addObject("password_error_info", errorInfo);
            mav.setViewName("login");
        } catch (LockedAccountException e) {
            String errorInfo = "用户" + token.getPrincipal() + "已被锁定";
            log.error(errorInfo);
            mav.addObject("password_has_error", true);
            mav.addObject("password_error_info", errorInfo);
            mav.setViewName("login");
        } catch (AuthenticationException e) {
            String errorInfo = "认证异常";
            log.error(e.getMessage(), e);
            mav.addObject("password_has_error", true);
            mav.addObject("password_error_info", errorInfo);
            mav.setViewName("login");
        } catch (Exception e) {
            String errorInfo = "认证异常";
            log.error(e.getMessage(), e);
            mav.addObject("password_has_error", true);
            mav.addObject("password_error_info", errorInfo);
            mav.setViewName("login");
        }

        /*
        org.apache.shiro.authc.
            CredentialsException            凭证不合法（密码错误）
            DisabledAccountException        账号被禁用
            LockedAccountException          账号被锁定
            ExpiredCredentialsException     凭证过期（登录超时）
            AuthenticationException         认证异常
         */

        return mav;
    }

    @PostMapping("/logout")
    public ModelAndView doLogout(ModelAndView mav) {
        log.info("注销");

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        mav.setViewName("index");
        return mav;
    }
}
