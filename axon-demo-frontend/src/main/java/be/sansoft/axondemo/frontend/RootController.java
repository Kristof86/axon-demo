package be.sansoft.axondemo.frontend;

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

    private final String accountsServiceRestUrl;
    private final String accountsServiceWsUrl;

    public RootController(
            @Value("${services.accounts.rest.url}") String accountsServiceRestUrl,
            @Value("${services.accounts.ws.url}") String accountsServiceWsUrl) {
        this.accountsServiceRestUrl = accountsServiceRestUrl;
        this.accountsServiceWsUrl = accountsServiceWsUrl;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("accountsServiceRestUrl", accountsServiceRestUrl);
        model.addAttribute("accountsServiceWsUrl", accountsServiceWsUrl);
        return "index";
    }
}
