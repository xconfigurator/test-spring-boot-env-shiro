package liuyang.testspringbootenvshiro.modules.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这个Controller主要是根据下面这个视频
 * https://www.bilibili.com/video/BV1ct411x7CN?p=4&vd_source=8bd7b24b38e3e12c558d839b352b32f4
 * 黑马的一个2小时课程 在Spring Boot环境中使用Apache Shiro
 *
 * @author xconf
 * @since 2022/9/3
 */
@Controller
@RequestMapping("20220903")
@Slf4j
public class Thymeleaf20220903Controller {

    @RequestMapping("foo")
    public String foo() {
        return "20220903/foo";
    }

    @RequestMapping("bar")
    public String bar(Model model) {
        model.addAttribute("bar", "bar");
        return "20220903/bar";
    }
}
