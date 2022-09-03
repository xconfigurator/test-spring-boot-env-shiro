package liuyang.testspringbootenvshiro.modules.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
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
