package JHboard.project.security.config;

import static org.springframework.security.config.Customizer.*;

import JHboard.project.domain.member.service.MemberService;
import JHboard.project.security.jwt.JwtFilter;
import JHboard.project.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        .authorizeHttpRequests(request -> request
            .requestMatchers("/board/new", "/board/*/edit", "/board/*/delete").authenticated()
            .requestMatchers("/admin").hasRole("ADMIN")
            .requestMatchers("/", "/login", "/register", "/board/{boardId}").permitAll());

    http
        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


//        http
//            .formLogin((auth) -> auth
//                    .loginPage("/login")
//                    .loginProcessingUrl("/login")
//                    .permitAll());

        http
        .formLogin((auth) -> auth.disable());

    http
        .httpBasic((auth) -> auth.disable());

    http //jwt 방식은 세션을 stateless하게 설정해야 한다.
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


//    http   //jwt 방식은 세션을 stateless하게 관리하기 때문에 csrf 공격을 방어할 필요가 없다?
//        .csrf((auth) -> auth.disable());
    return http.build();
  }

}
