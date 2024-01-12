package JHboard.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    String authorization = request.getHeader("Authorization");

    //헤더 검증
    if(authorization == null || !authorization.startsWith("Bearer ")){
      log.info("token = {}", authorization);
      filterChain.doFilter(request, response);

      //조건이 해당되면(토큰이 없거나 잘못 되었다면) 메소드 종료(필수)
      return;
    }

    log.info("authorization now = {}", authorization);
    //Bearer 부분 제거 후 순수 토큰만 획득
    String token = authorization.split(" ")[1];

    if(jwtUtil.isExpired(token)) {
      log.info("token is expired");
      filterChain.doFilter(request,response);
      return;
    }

    //토큰에서 username, nickname, role 확득
    String username = jwtUtil.getUsername(token);
    String nickname = jwtUtil.getNickname(token);
    String memberRole = jwtUtil.getMemberRole(token);

    

  }
}
