package JHboard.project.domain.member.service;

import JHboard.project.domain.member.dto.oauth2.CustomOAuth2User;
import JHboard.project.domain.member.dto.oauth2.GoogleResponse;
import JHboard.project.domain.member.dto.oauth2.NaverResponse;
import JHboard.project.domain.member.dto.oauth2.OAuth2Response;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.entity.MemberRole;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;
//  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    log.info("attribute={}", oAuth2User.getAttributes());

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = null;
    if(registrationId.equals("naver")){
      oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
    } else if (registrationId.equals("google")) {
      oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
    } else{
      return null;
    }

    Member member;
    if(!checkOauthUser(oAuth2Response)){
      member = createMember(oAuth2Response);
      memberRepository.save(member);
    } else{
      member = memberRepository.findByUsername(oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId()).get();
    }
    // 여기서 JWT 토큰 생성 및 쿠키 추가
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    createCookie(response, member);


    return new CustomOAuth2User(oAuth2Response, "ROLE_MEMBER");
  }


  /**
   * 현재 로그인을 시도한 유저의 정보가 db에 저장되어 있는지 확인
   * @param oAuth2Response
   * @return
   */
  private boolean checkOauthUser(OAuth2Response oAuth2Response) {
    String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    Optional<Member> existMember = memberRepository.findByUsername(username);
    if(existMember.isEmpty()) { //로그인 했던 적이 없다면
      return false;
    }
    return true;
  }

  private Member createMember(OAuth2Response oAuth2Response){
    String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
    String nickname = oAuth2Response.getName();
    Member member = Member.CreateEntity(username, nickname);
    return member;
  }

  private void createCookie(HttpServletResponse response, Member member) {
    Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER
        , jwtUtil.createToken(member.getUsername(), member.getMemberRole()));
    cookie.setMaxAge(7 * 24 * 60 * 60); //7일 동안 유효
    cookie.setPath("/");
    cookie.setDomain("localhost");
    cookie.setSecure(false);

    response.addCookie(cookie);
  }
}
