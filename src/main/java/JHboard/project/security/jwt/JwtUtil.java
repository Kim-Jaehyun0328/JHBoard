package JHboard.project.security.jwt;


import JHboard.project.domain.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtUtil {

  @Value("${spring.jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final MacAlgorithm signatureAlgorithm = SIG.HS256;

  private final UserDetailsService userDetailsService;

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long TOKEN_TIME = 60 * 60 * 1000L;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    //헤더에 토큰이 없다면
    if(bearerToken == null) {
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
  public boolean validateToken(String token) {
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
    return false;
  }

  //토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  //인증 객체 생성
  public Authentication createAuthentication(String username){
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

}


