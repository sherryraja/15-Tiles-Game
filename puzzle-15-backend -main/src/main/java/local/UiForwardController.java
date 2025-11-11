package local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiForwardController {
    @GetMapping("/ui")
    public String ui() { return "forward:/ui/index.html"; }
    @GetMapping("/ui/")
    public String uiSlash() { return "forward:/ui/index.html"; }
}
