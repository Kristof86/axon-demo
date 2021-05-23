package be.craftworkz.axondemo.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kristofennekens
 */
@Controller
public class RootController {

    private final String accountsCommandServiceRestUrl;
    private final String accountsQueryServiceRestUrl;

    public RootController(
            @Value("${services.accounts.command.rest.url}") String accountsCommandServiceRestUrl,
            @Value("${services.accounts.query.rest.url}") String accountsQueryServiceRestUrl) {
        this.accountsCommandServiceRestUrl = accountsCommandServiceRestUrl;
        this.accountsQueryServiceRestUrl = accountsQueryServiceRestUrl;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("accountsCommandServiceRestUrl", accountsCommandServiceRestUrl);
        model.addAttribute("accountsQueryServiceRestUrl", accountsQueryServiceRestUrl);
        return "index";
    }
}
