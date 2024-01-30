package JHboard.project.domain.board.service;


import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.entity.BoardFile;
import JHboard.project.domain.board.repository.BoardRepository;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.repository.CommentRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.global.s3.S3UploadSevice;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService{

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;
  private final S3UploadSevice s3UploadSevice;
  private final BoardFileService boardFileService;
  private final CommentRepository commentRepository;


  @Override
  public Optional<Board> findById(Long boardId) {
    return boardRepository.findById(boardId);
  }

  @Override
  public Page<Board> findAll(Pageable pageable) {
    return boardRepository.findAll(pageable);
  }

  @Override
  public Page<Board> searchBoards(String keyword, Pageable pageable) {
    return boardRepository.searchBoards(keyword, pageable);
  }


  @Override
  @Transactional
  public void create(BoardRqDto boardRqDto, Principal principal) throws IOException {
    String username = principal.getName();
    Optional<Member> memberOptional = memberRepository.findByUsername(username);
    if(!memberOptional.isEmpty()){
      List<BoardFile> boardFiles = s3UploadSevice.saveFiles(boardRqDto.getMultipartFileList());
      Board savedBoard = boardRepository.save(
          Board.createEntity(boardRqDto, memberOptional.get(), boardFiles));

      for (BoardFile boardFile : boardFiles) {
        boardFile.connetBoardId(savedBoard);
        boardFileService.create(boardFile);
      }
    }
  }

  @Override
  @Transactional
  public void delete(Long boardId) {

    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + boardId));
    for (BoardFile boardFile : board.getBoardFiles()) {
      boardFileService.delete(boardFile);
    }
    boardRepository.deleteById(boardId);
  }

  @Override
  @Transactional
  public void updateBoard(Long boardId, BoardRqDto boardRqDto) throws IOException {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + boardId));

    List<BoardFile> boardFiles = boardFileService.deleteAndCreateBoardFile(boardRqDto, board);
    board.updateBoard(boardRqDto, boardFiles);

  }

  /**
   * 로그인 유저 == 작성자 -> 버튼 막아두기
   * 로그인 유지 != 작성자 -> 버튼 활성화 (리포지토리 뒤져서 활성화)
   * 비로그인 유저 -> 누르면 로그인 창으로
   * @param boardId
   * @return
   */
  @Transactional
  public BoardRsDto detailBoard(Long boardId) {
    Optional<Board> boardOptional = boardRepository.findById(boardId);
    if(!boardOptional.isEmpty()){
      boardOptional.get().updateView();
      List<Comment> comments = commentRepository.findByParentIsNullAndBoardId(boardId);
      return BoardRsDto.toDto(boardOptional.get(), comments);
    }
    return null;
  }

  /**
   * 현재 로그인한 사용자와 특정 게시글 작성자가 같은 지 확인하는 메소드
   * @param boardId
   * @param principal
   * @return true면 같다, 다르면 false
   */
  @Override
  public boolean checkUser(Long boardId, Principal principal) { //로그인 한 유저와 게시글 작성 유저가 같은 지 확인
    if(principal == null){
      return false;
    }
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
