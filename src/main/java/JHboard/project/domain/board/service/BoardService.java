package JHboard.project.domain.board.service;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import java.util.List;
import java.util.Optional;

public interface BoardService {
  Optional<Board> findById(Long boardId);

  List<Board> findAll();

  Board create(Board board);

  void delete(Long boardId);

  void updateBoard(Long boardId, BoardRqDto boardRqDto);

}
