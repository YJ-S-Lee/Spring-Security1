package spring.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.security.dto.LoginForm;
import spring.security.service.LoginService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    public final LoginService loginService;

    //로그인 폼을 보여주는 로직
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("page", "login");
        return "user/loginForm";
    }
}