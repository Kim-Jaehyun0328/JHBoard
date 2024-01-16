package JHboard.project.domain.board.service;


import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.dto.BoardRsDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.entity.BoardFile;
import JHboard.project.domain.board.repository.BoardRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.global.s3.S3UploadSevice;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService{

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;
  private final S3UploadSevice s3UploadSevice;
  private final BoardFileService boardFileService;


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

  @Transactional
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
