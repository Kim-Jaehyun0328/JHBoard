package JHboard.project.domain.board.dto;


import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.comment.dto.CommentRsDto;
import JHboard.project.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BoardRsDto {

  private Long id;
  private String writer; //작성자의 username
  private String title;
  private String content;
  private int views;
  private int likeCount;
  private int commentCnt;
  private List<BoardFileDto> boardFileDtoList = new ArrayList<>();
  private List<CommentRsDto> comments = new ArrayList<>();
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;


  /**
   * 게시글 리스트에를 위한 DTO 최소한의 정보만 뽑아온다.
   * @param board
   * @return
   */
  public static BoardRsDto toDtoForList(Board board){
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.writer = board.getMember().getNickname();
    boardRsDto.title = board.getTitle();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    boardRsDto.commentCnt = board.getCommentCnt();
    boardRsDto.createdAt = board.getCreatedAt();
    return boardRsDto;
  }

  /**
   * 디테일한 게시글을 위한 DTO 첨부파일 등 구체적인 정보들을 뽑아온다.
   * @param board
   * @return
   */
  public static BoardRsDto toDto(Board board, List<Comment> comments) { //디테일
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.writer = board.getMember().getUsername(); //여기서 쿼리가 더 나갈 거야
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    boardRsDto.commentCnt = board.getCommentCnt();
    boardRsDto.boardFileDtoList = board.getBoardFiles().stream()
        .map(bf -> new BoardFileDto(bf)).collect(Collectors.toList());
    boardRsDto.comments =comments.stream()
        .map(c -> CommentRsDto.toDto(c)).collect(Collectors.toList());
    return boardRsDto;
  }

  public static BoardRsDto toDtoEdit(Board board) { //편집 창(edit)
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.writer = board.getMember().getUsername(); //여기서 쿼리가 더 나갈 거야
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.boardFileDtoList = board.getBoardFiles().stream()
        .map(bf -> new BoardFileDto(bf)).collect(Collectors.toList());
    return boardRsDto;
  }
}
