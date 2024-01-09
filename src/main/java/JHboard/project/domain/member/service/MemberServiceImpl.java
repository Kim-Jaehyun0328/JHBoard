package JHboard.project.domain.member.service;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;


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
  public Member create(Member member) {
    return memberRepository.save(member);
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

  public boolean validateLogin(LoginRqDto loginRqDto) {
    Optional<Member> member = memberRepository.findByUsername(loginRqDto.getUsername());

    return member.map(m -> loginRqDto.getPassword().equals(m.getPassword())).orElse(false);
  }
}
