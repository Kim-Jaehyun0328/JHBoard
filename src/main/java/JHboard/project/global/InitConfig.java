package JHboard.project.global;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.repository.BoardRepository;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitConfig {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final BoardRepository boardRepository;

  @Bean
  public boolean initialData() {
    Member member1 = Member.createEntity(new RegisterRqDto("userA", "hello",
            "123123123", "123123123"),
        passwordEncoder);
    Member member2 = Member.createEntity(new RegisterRqDto("userB", "hi",
            "123123123", "123123123"),
        passwordEncoder);
    Member member3 = Member.createEntity(new RegisterRqDto("userC", "good",
            "123123123", "123123123"),
        passwordEncoder);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);

    for(int i = 0; i<5; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member1, null);
      boardRepository.save(board);
    }

    for(int i = 5; i<10; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member2, null);
      board.updateLikeCount((int) (Math.random()*200));
      board.updateCommentCount((int) (Math.random()*200));
      boardRepository.save(board);
    }
    for(int i = 10; i<15; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member3, null);
      board.updateLikeCount((int) (Math.random()*200));
      board.updateCommentCount((int) (Math.random()*200));
      boardRepository.save(board);
    }

    for(int i = 15; i<120; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"), member3, null);
      board.updateLikeCount((int) (Math.random()*200));
      board.updateCommentCount((int) (Math.random()*200));
      boardRepository.save(board);
    }

    return true;
  }

}
