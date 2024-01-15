package JHboard.project.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = jwtUtil.resolveToken(request);


    if(token != null) {
      if(!jwtUtil.validateToken(token)){ // 잘못된 토큰이라면
        throw new IllegalArgumentException("JWT Token 인증 실패");
      }

      Claims info = jwtUtil.getUserInfoFromToken(token);
      setAuthentication(info.getSubject());
    }

    filterChain.doFilter(request,response);
  }


  public void setAuthentication(String username) {
    //jwt 인증 성공 시
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = jwtUtil.createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }
}
