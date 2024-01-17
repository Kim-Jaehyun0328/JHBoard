package JHboard.project.global.security.config;


import JHboard.project.global.security.jwt.JwtFilter;
import JHboard.project.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtUtil jwtUtil;

  @Bean  //이걸로 바꿔야 함
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/board/{boardId}/edit", "/board/{boardId}/delete").hasRole("MEMBER")
            .requestMatchers("/board/new", "/logout").authenticated()
            .requestMatchers("/admin").hasRole("ADMIN")
            .anyRequest().permitAll()
        );

    http
        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        http
        .formLogin((auth) -> auth.disable());

    http
        .httpBasic((auth) -> auth.disable());

    http
        .logout((auth) -> auth.disable());

    http //jwt 방식은 세션을 stateless하게 설정해야 한다.
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


    http   //jwt 방식은 세션을 stateless하게 관리하기 때문에 csrf 공격을 방어할 필요가 없다?
        .csrf((auth) -> auth.disable());

    http
        .exceptionHandling((exceptionHandling) -> exceptionHandling
        .authenticationEntryPoint((request, response, authException) ->
            response.sendRedirect("/login"))
    );
    return http.build();
  }

}
