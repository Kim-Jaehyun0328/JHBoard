package JHboard.project.domain.board.service;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface BoardService {
  Optional<Board> findById(Long boardId);

  List<Board> findAll();

  Board create(BoardRqDto boardRqDto, Principal principal);

  void delete(Long boardId);

  void updateBoard(Long boardId, BoardRqDto boardRqDto);

  BoardRsDto detailBoard(Long boardId); //조회 수 1 증가 + 게시글 정보

  boolean checkUser(Long boardId, Principal principal);

}
