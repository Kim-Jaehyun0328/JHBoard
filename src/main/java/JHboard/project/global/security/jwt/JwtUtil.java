package JHboard.project.global.security.jwt;


import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtUtil {


  private Key key;
  private UserDetailsService userDetailsService;

  public JwtUtil(@Value("${spring.jwt.secret.key}")String secret, UserDetailsService userDetailsService) {
    byte[] byteSecretKey = Decoders.BASE64.decode(secret);
    key = Keys.hmacShaKeyFor(byteSecretKey);
    this.userDetailsService =userDetailsService;
  }



  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer:";  //(Bearer ) 이런식으로 한 칸 띄우면 쿠키 정책에 위반돼서 예외뜸
  private static final long TOKEN_TIME = 60 * 60 * 1000L; //토큰의 유효 시간은 한 시간


  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    //헤더에 토큰이 없다면
    if(bearerToken == null || bearerToken.startsWith("Basic")) {
      Cookie[] cookies = request.getCookies();
      if(cookies != null) {
        for (Cookie cookie : cookies) {
          String name = cookie.getName();
          String value = cookie.getValue();
          if(name.equals(AUTHORIZATION_HEADER)) {
            bearerToken = value;
          }
        }
      }
    }
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }

    return null;
  }

  //토큰 생성
  public String createToken(String username, MemberRole role) {
    Date date = new Date();
    String token = BEARER_PREFIX + Jwts.builder()
        .subject(username)
        .claim(AUTHORIZATION_KEY, role)
        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    return token;
  }

  //토큰 생성
  public boolean validateToken(String token, HttpServletResponse response) throws IOException {
    try {
      Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    clearJwtCookie(response);
    return false;
  }

  //토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    log.info("getUserInfoFromToken");
    return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  //인증 객체 생성
  public Authentication createAuthentication(String username){
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  private void clearJwtCookie(HttpServletResponse response) throws IOException {
    Cookie jwtCookie = new Cookie(AUTHORIZATION_HEADER, null);
    jwtCookie.setValue(null);
    jwtCookie.setMaxAge(0);
    jwtCookie.setPath("/");
    response.addCookie(jwtCookie);
    response.sendRedirect("/");
  }


}


