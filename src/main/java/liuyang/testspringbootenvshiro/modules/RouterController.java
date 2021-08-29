package liuyang.testspringbootenvshiro.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @since 2021/8/29
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
}
