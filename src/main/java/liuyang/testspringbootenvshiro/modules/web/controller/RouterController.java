package liuyang.testspringbootenvshiro.modules.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @scine 2021/4/1
 * @update 2021/8/29 合并hello.controller.HelloController
 */
@Controller
@Slf4j
public class RouterController {
    // /////////////////////////////////////////////////////////////////
    // ShiroConfig中的shiroFilterFactoryBean使用的跳转路径
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/401")
    public String unauthorized() {
        return "error/401";
    }

    // /////////////////////////////////////////////////////////////////
    // 功能页面
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    // /////////////////////////////////////////////////////////////////
    // 看看基本功能好不好使
    @RequestMapping({"/"})
    public ModelAndView hello(ModelAndView  mav) {
        mav.addObject("msg", "hello, Shiro");
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("/exception")
    public String testException() {
        throw new RuntimeException("测试异常处理！");
        // return "";
    }
}
