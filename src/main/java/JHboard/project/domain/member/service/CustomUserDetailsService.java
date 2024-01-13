package JHboard.project.domain.member.service;

import JHboard.project.domain.member.dto.CustomUserDetails;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Im in UserDetails");
    Optional<Member> member = memberRepository.findByUsername(username);
    if (member.isEmpty()) {
      throw new UsernameNotFoundException(username + "을 찾을 수 없습니다.");
    }
    return new CustomUserDetails(member.get());
  }
}