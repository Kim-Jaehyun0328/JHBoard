package JHboard.project;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.domain.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {

  private final BoardService boardService;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void createBoard() {
    for(int i = 0; i<20; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"));
      boardService.create(board);
    }

    Member member1 = Member.createEntity(new RegisterRqDto("hello", "123123",
            "11111111", "11111111"),
        passwordEncoder);
    Member member2 = Member.createEntity(new RegisterRqDto("hi", "111111",
            "22222222", "22222222"),
        passwordEncoder);
    Member member3 = Member.createEntity(new RegisterRqDto("good", "222222",
            "33333333", "33333333"),
        passwordEncoder);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);

  }

}
