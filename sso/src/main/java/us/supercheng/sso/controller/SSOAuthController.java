package us.supercheng.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class SSOAuthController {

    @RequestMapping("login")
    public String login(@RequestParam("retUrl") String retUrl,
                        Model model) {
        model.addAttribute("retUrl", retUrl);
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("retUrl") String retUrl,
                          Model model) {
        model.addAttribute("username" , username);
        model.addAttribute("password" , password);


        boolean loginSuccess = false | true;
        // Login Success

        if (loginSuccess) {
            return "redirect:http://" + retUrl;
        }


        // Login Failed
        return "login";
    }
}
