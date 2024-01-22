package JHboard.project.domain.board.dto;


import JHboard.project.domain.board.entity.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BoardRsDto {

  private Long id;
  private String writer; //작성자의 username
  private String title;
  private String content;
  private int views;
  private int likeCount;
  private List<BoardFileDto> boardFileDtoList = new ArrayList<>();


  /**
   * 게시글 리스트에를 위한 DTO 최소한의 정보만 뽑아온다.
   * @param board
   * @return
   */
  public static BoardRsDto toDtoForList(Board board){
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    return boardRsDto;
  }

  /**
   * 디테일한 게시글을 위한 DTO 첨부파일 등 구체적인 정보들을 뽑아온다.
   * @param board
   * @return
   */
  public static BoardRsDto toDto(Board board) { //디테일
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.writer = board.getMember().getUsername(); //여기서 쿼리가 더 나갈 거야
    log.info("lazy 로딩 멤버 쿼리 나갑니다.");
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    boardRsDto.boardFileDtoList = board.getBoardFiles().stream()
        .map(bf -> new BoardFileDto(bf)).collect(Collectors.toList());
    return boardRsDto;
  }
}
