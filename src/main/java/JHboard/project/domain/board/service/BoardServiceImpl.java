package JHboard.project.domain.board.service;


import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.repository.BoardRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import java.security.Principal;
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
  private final MemberRepository memberRepository;


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
  public Board create(BoardRqDto boardRqDto, Principal principal) {
    String username = principal.getName();
    Optional<Member> memberOptional = memberRepository.findByUsername(username);

    if(!memberOptional.isEmpty()){
      return boardRepository.save(Board.createEntity(boardRqDto, memberOptional.get()));
    }
    return null;
  }

  @Override
  @Transactional
  public void delete(Long boardId) {
    Optional<Board> board = boardRepository.findById(boardId);
    if(board.isEmpty()){
      throw new RuntimeException("존재하지 않은 게시글입니다.");
    }
    boardRepository.deleteById(boardId);
  }

  @Override
  @Transactional
  public void updateBoard(Long boardId, BoardRqDto boardRqDto) {
    Optional<Board> board = boardRepository.findById(boardId);
    if(board.isEmpty()){
      throw new RuntimeException("존재하지 않는 게시글입니다.");
    }
    board.get().updateBoard(boardRqDto);
  }

  public BoardRsDto detailBoard(Long boardId) {
    Optional<Board> boardOptional = boardRepository.findById(boardId);
    if(!boardOptional.isEmpty()){
      boardOptional.get().updateView();
      return BoardRsDto.toDto(boardOptional.get());
    }
    return null;
  }

  @Override
  public boolean checkUser(Long boardId, Principal principal) { //로그인 한 유저와 게시글 작성 유저가 같은 지 확인
    String loggedInUsername = principal.getName();
    Optional<Board> boardOptional = boardRepository.findById(boardId);

    if(!boardOptional.isEmpty()){
      Board board = boardOptional.get();
      String boardUsername = board.getMember().getUsername();
      if(loggedInUsername.equals(boardUsername)){
        return true;
      }
    }





    return false;
  }
}
