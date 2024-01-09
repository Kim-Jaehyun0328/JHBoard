package JHboard.project.domain.member.controller;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;


  @GetMapping("/login")
  public String getLogin(@ModelAttribute("loginForm")LoginRqDto loginRqDto){
    return "";
  }

  @PostMapping("/login")
  public String postLogin(@ModelAttribute("loginForm") LoginRqDto loginRqDto, HttpSession session) {
    if(memberService.validateLogin(loginRqDto)){
      session.setAttribute("loggedInMember", loginRqDto.getUsername());
      return "redirect:/";
    }
    return "members/loginForm";
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute("registerForm") RegisterRqDto registerRqDto) {
    return "members/registerForm";
  }

  @PostMapping("/register")
  public String postRegister(@Valid @ModelAttribute("registerForm") RegisterRqDto registerRqDto, BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
      return "members/registerForm";
    }

    memberService.create(Member.createEntity(registerRqDto));
    return "redirect:/";
  }


}
