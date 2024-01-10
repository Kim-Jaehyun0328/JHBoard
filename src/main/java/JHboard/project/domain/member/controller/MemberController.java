package JHboard.project.domain.member.controller;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;


  @GetMapping("/login")
  public String getLogin(@ModelAttribute("loginForm")LoginRqDto loginRqDto){
    return "members/loginForm";
  }

  @PostMapping("/login")
  public String postLogin(@ModelAttribute("loginForm") LoginRqDto loginRqDto, HttpSession session) {
    return "members/loginForm";
  }

  @GetMapping("/register")
  public String getRegister(Model model) {
    model.addAttribute("registerRqDto", new RegisterRqDto());
    return "members/registerForm";
  }

  @PostMapping("/register")
  public String postRegister(@Valid @ModelAttribute("registerRqDto") RegisterRqDto registerRqDto,
      BindingResult bindingResult, Model model) {
    if(bindingResult.hasErrors()){
      return "members/registerForm";
    }

    try{
      log.info("registerForm = {}", registerRqDto.toString());
      memberService.create(registerRqDto);
    } catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "members/registerForm";
    }

    return "redirect:/";
  }


}
