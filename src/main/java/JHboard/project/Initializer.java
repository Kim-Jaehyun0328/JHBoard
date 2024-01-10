package JHboard.project.config;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {

  private final BoardService boardService;

  @PostConstruct
  public void createBoard() {
    for(int i = 0; i<20; i++){
      Board board = Board.createEntity(
          new BoardRqDto("이것은 " + String.valueOf(i) + "번째 게시글", "안녕하세요"));
      boardService.create(board);
    }
  }

}
