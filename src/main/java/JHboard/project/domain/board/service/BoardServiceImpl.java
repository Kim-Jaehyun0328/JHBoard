package JHboard.project.domain.board.service;


import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.repository.BoardRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{

  private final BoardRepository boardRepository;


  @Override
  public Optional<Board> findById(Long boardId) {
    return boardRepository.findById(boardId);
  }

  @Override
  public List<Board> findAll() {
    return boardRepository.findAll();
  }

  @Override
  @Transactional
  public Board create(Board board) {
    return boardRepository.save(board);
  }

  @Override
  @Transactional
  public void delete(Long boardId) {
    boardRepository.deleteById(boardId);
  }
}
