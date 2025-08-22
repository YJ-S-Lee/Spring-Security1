package spring.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.security.domain.EzenMember;
import spring.security.dto.MemberForm;
import spring.security.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("page", "add");
        return "user/addMemberForm";
    }

    //회원 가입
    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user/addMemberForm";
        }

        EzenMember ezenMember = new EzenMember();
        ezenMember.setLoginId(memberForm.getLoginId());
        ezenMember.setName(memberForm.getName());
        ezenMember.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        ezenMember.setGrade("user");

        memberService.join(ezenMember);
        return "redirect:/";
    }

    //회원 목록
    @GetMapping(value = "/members")
    public String list(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); //현재 로그인한 사용자 이름
            model.addAttribute("loginMember", username);
        }
        List<EzenMember> members = memberService.findMemberList();
        model.addAttribute("members", members);
        //model.addAttribute("loginMember", loginMember);
        return "admin/memberList";
    }

    //수정하기(수정폼 보여주기)
    @GetMapping("/members/{id}/edit")
    public String updateMemberForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); //현재 로그인한 사용자 이름
            model.addAttribute("loginMember", username);
        }

        EzenMember ezenMember = memberService.findOneMember(id).get();

        MemberForm memberForm = new MemberForm();

        memberForm.setId(ezenMember.getId());
        memberForm.setLoginId(ezenMember.getLoginId());
        memberForm.setName(ezenMember.getName());
        memberForm.setPassword(ezenMember.getPassword());
        memberForm.setGrade(ezenMember.getGrade());

        model.addAttribute("memberForm", memberForm);

        return "admin/updateMemberForm";
    }

    //수정하기(수정폼 저장하기)
    @PostMapping("/members/{id}/edit")
    public String updateMemberFormSave(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult bindingResult, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); //현재 로그인한 사용자 이름
            model.addAttribute("loginMember", username);
        }
        if(bindingResult.hasErrors()) {
            return "admin/updateMemberForm";
        }

        EzenMember ezenMember = new EzenMember();

        ezenMember.setId(memberForm.getId());
        ezenMember.setLoginId(memberForm.getLoginId());
        ezenMember.setName(memberForm.getName());
        ezenMember.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        ezenMember.setGrade(memberForm.getGrade());

        memberService.update(ezenMember);
        return "redirect:/members";
    }

    //삭제하기
    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Optional<EzenMember> findMember = memberService.findOneMember(id);
        if(findMember.isPresent()) {
            EzenMember ezenMember = findMember.get();
            memberService.deleteMember(ezenMember.getId());
        }
        return "redirect:/members";
    }
    
}