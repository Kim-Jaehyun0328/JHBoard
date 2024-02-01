package JHboard.project.domain.like.controller;

import JHboard.project.domain.like.service.LikeService;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.domain.member.service.MemberService;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikeController {
  private final LikeService likeService;
//  private final MemberService memberService;
  private final MemberRepository memberRepository;

  @PostMapping("/like/{boardId}")
  @ResponseBody
  public boolean like(@PathVariable("boardId") Long boardId, Principal principal) {
    log.info("hello");
    if(principal != null) {
      String username = principal.getName();
      Member member = memberRepository.findByUsername(username).get();
      boolean result = likeService.saveLike(boardId, member.getId());
      return result;
    }

    return false;
  }

}
