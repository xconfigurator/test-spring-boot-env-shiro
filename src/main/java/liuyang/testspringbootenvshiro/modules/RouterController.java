package liuyang.testspringbootenvshiro.modules;

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

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    ////////////////////////////////////////////////
    @RequestMapping({"/"})
    public ModelAndView hello(ModelAndView  mav) {
        mav.addObject("msg", "hello, Shiro");
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/exception")
    public String testException() {
        throw new RuntimeException("测试异常处理！");
        // return "";
    }
}
