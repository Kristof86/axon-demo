package be.sansoft.axondemo.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kristofennekens
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("contextPath", request.getContextPath());
        return "index";
    }
}
