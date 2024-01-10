package JHboard.project.domain.member.repository;

import JHboard.project.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUsername(String username); //회원가입 검증
  Optional<Member> findByNickname(String nickname); //회원가입 검증
}
