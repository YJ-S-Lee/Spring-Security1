package spring.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import spring.security.domain.EzenMember;

@Controller
@RequiredArgsConstructor
public class HomeController {

    //@SessionAttribute 스프링이 제공하는 세션, 이미 로그인 된 사용자를 찾을 때 사용, 세션을 생성하지 않음.
    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); //현재 로그인한 사용자 이름
            model.addAttribute("loginMember", username);
            return "loginHome";
        }
        return "home";  //Thymeleaf 템플릿 이름
    }
}