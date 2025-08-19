package spring.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spring.security.domain.EzenMember;
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

    //로그인이 되는 로직
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "user/loginForm";
        }

        EzenMember loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        //log.info("로그인한 맴버의 아이디는? {}", loginMember.getLoginId());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "user/loginForm";
        }

        //로그인 성공 시 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원정보를 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        log.info("로그인한 회원은? {} {}", loginMember.getLoginId(), loginMember.getName());
        log.info("session {}", session);
        return "redirect:/";
    }

    //로그아웃 기능
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션 삭제(기본값은 true이지만 삭제해야하기에 false를 준다.), 일단 세션을 가지고 오지만 없으면 null 값을 준다.
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();   //세션 제거
        }

        return "redirect:/";
    }

}