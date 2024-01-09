package JHboard.project.domain.member.controller;


import JHboard.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;


  @GetMapping("/login")
  public String getLogin(){
    return "";
  }

  @PostMapping("/login")
  public String postLogin() {
    return "";
  }

  @GetMapping("/register")
  public String getRegister(){
    return "";
  }

  @PostMapping("/register")
  public String postRegister() {
    return "";
  }
}
