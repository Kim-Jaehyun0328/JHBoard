package JHboard.project.security.config;

import static org.springframework.security.config.Customizer.*;

import JHboard.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

//  private final MemberService memberService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(request -> request
            .requestMatchers("/board/new", "/board/{boardId}/edit", "/board/{boardId}/delete").authenticated()
            .requestMatchers("/admin").hasRole("ADMIN")
            .requestMatchers("/", "/login", "/register", "/board/{boardId}").permitAll());


        http
            .formLogin((auth) -> auth
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .permitAll());
        http
            .logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/"));

//    http
//        .csrf((auth) -> auth.disable());

    return http.build();
  }

  @Bean  //이걸로 바꿔야 함
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return NoOpPasswordEncoder.getInstance();
//  }


}
