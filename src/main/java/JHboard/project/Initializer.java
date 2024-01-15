package JHboard.project;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.repository.BoardRepository;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final BoardRepository boardRepository;

  @PostConstruct
  public void createBoard() {
    Member member1 = Member.createEntity(new RegisterRqDto("hello", "hello",
            "123123123", "123123123"),
        passwordEncoder);
    Member member2 = Member.createEntity(new RegisterRqDto("hi", "hi",
            "123123123", "123123123"),
        passwordEncoder);
    Member member3 = Member.createEntity(new RegisterRqDto("good", "good",
            "123123123", "123123123"),
        passwordEncoder);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);

    for(int i = 0; i<5; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member1);
      boardRepository.save(board);
    }

    for(int i = 5; i<10; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member2);
      boardRepository.save(board);
    }
    for(int i = 10; i<15; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member3);
      boardRepository.save(board);
    }
  }

}
