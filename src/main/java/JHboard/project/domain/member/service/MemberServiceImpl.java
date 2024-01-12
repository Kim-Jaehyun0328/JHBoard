package JHboard.project.domain.member.service;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import JHboard.project.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   *
   * @param memberId
   * @return optinal 형태로 감싸서 반환한다. 비어있다면 예외 처리
   */
  @Override
  public Optional<Member> findById(Long memberId) {
    return memberRepository.findById(memberId);
  }

  /**
   *
   * @return 멤버 엔티티 모두 반환한다.
   */
  @Override
  public List<Member> findAll() {
    return memberRepository.findAll();
  }

  /**
   *
   * @param member
   * @return 멤버엔티티를 그대로 반환한다.
   */
  @Override
  @Transactional
  public Member create(RegisterRqDto registerRqDto) {
    validateDuplicateMember(registerRqDto);
    Member member = Member.createEntity(registerRqDto, passwordEncoder); //passwordEncoder는?

    return memberRepository.save(member);
  }

  @Override
  public void login(LoginRqDto loginRqDto, HttpServletResponse response) {
    Optional<Member> optionalMember = memberRepository.findByUsername(loginRqDto.getUsername());

    if(optionalMember.isEmpty()){
      log.warn("회원이 존재하지 않음");
      throw new IllegalArgumentException("존재하는 아이디가 없습니다.");
    }

    Member member = optionalMember.get();

    if(!passwordEncoder.matches(loginRqDto.getPassword(), member.getPassword())){
      log.warn("비밀번호가 일치하지 않습니다.");
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    //로그인 가능
  }


  /**
   * 멤버 엔티티를 db로부터 제거한다.
   * @param memberId
   */
  @Override
  @Transactional
  public void delete(Long memberId) {
    memberRepository.deleteById(memberId);
  }

  private void validateDuplicateMember(RegisterRqDto registerRqDto) {
    Optional<Member> checkUsername = memberRepository.findByUsername(registerRqDto.getUsername());
    if(!checkUsername.isEmpty()) { //사용할 수 없는 아이디라면
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
    Optional<Member> checkNickname = memberRepository.findByNickname(registerRqDto.getNickname());
    if(!checkNickname.isEmpty()){
      throw new IllegalStateException("중복된 닉네임이 있습니다.");
    }

    if(!registerRqDto.getPassword().equals(registerRqDto.getPasswordConfirm())){
      throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
    }
  }
}
