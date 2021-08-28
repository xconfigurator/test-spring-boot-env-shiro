package liuyang.testspringbootenvshiro.modules.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liuyang
 * @scine 2021/4/1
 */
@Controller
public class HelloController {

    @RequestMapping({"/"})
    public ModelAndView hello(ModelAndView  mav) {
        mav.addObject("msg", "hello, Shiro");
        mav.setViewName("index");
        return mav;
    }
/*

    @RequestMapping("/main")
    public ModelAndView main(ModelAndView mav) {
        mav.setViewName("main");
        return mav;
    }

    @RequestMapping("/manager")
    public ModelAndView manager(ModelAndView mav) {
        mav.setViewName("manager");
        return mav;
    }

    @RequestMapping("/admin")
    public ModelAndView admin(ModelAndView mav) {
        mav.setViewName("admin");
        return mav;
    }
*/

    @GetMapping("/exception")
    public String testException() {
        throw new RuntimeException("测试异常处理！");
        // return "";
    }

    // 若是单纯的页面跳转可以这样来写
    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url) {
        return url;
    }
    // 当然也有其他的解决方案 TODO

}
