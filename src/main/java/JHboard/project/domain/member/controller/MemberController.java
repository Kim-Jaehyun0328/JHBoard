package JHboard.project.domain.member.controller;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;


  @GetMapping("/login")
  public String getLogin(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("loginForm")LoginRqDto loginRqDto){
    if(userDetails != null) {
      log.info("로그인 된 유저입니다. = {}", userDetails.getUsername());
      return "redirect:/";
    }
    return "members/loginForm";
  }


  @PostMapping("/login")
  public String postLogin(@ModelAttribute("loginForm") LoginRqDto loginRqDto, HttpServletResponse response, Model model) {
    log.info("Im in member controller (postLogin)");
    try{
      memberService.login(loginRqDto, response);
    } catch (IllegalArgumentException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "members/loginForm";
    }

    return "redirect:/";
  }

  @GetMapping("/logout")
  public String logout(@CookieValue(value = "Authorization", defaultValue = "", required = false)
                        Cookie jwtCookie, HttpServletResponse response) {
    clearJwtCookie(jwtCookie, response);
    return "redirect:/";
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

    return "redirect:/login";
  }

  @GetMapping("/admin")
  public String adminP(){
    return "members/admin";
  }


  private void clearJwtCookie(Cookie jwtCookie, HttpServletResponse response){
    jwtCookie.setValue(null);
    jwtCookie.setMaxAge(0);
    jwtCookie.setPath("/");
    response.addCookie(jwtCookie);
  }
}
