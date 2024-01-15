package JHboard.project.domain.member.service;


import JHboard.project.domain.member.dto.LoginRqDto;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {
  Optional<Member> findById(Long memberId);

  List<Member> findAll();

  Member create(RegisterRqDto registerRqDto);

  void login(LoginRqDto loginRqDto, HttpServletResponse response);

  void delete(Long memberId);

}
