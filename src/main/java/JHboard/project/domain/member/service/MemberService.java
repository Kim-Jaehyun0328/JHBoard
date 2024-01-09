package JHboard.project.domain.member.service;


import JHboard.project.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {
  Optional<Member> findById(Long memberId);

  List<Member> findAll();

  Member create(Member member);

  void delete(Long memberId);
}
