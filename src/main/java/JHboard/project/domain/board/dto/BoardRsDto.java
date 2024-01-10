package JHboard.project.domain.board.dto;


import JHboard.project.domain.board.entity.Board;
import lombok.Data;

@Data
public class BoardRsDto {

  private Long id;
  private String title;
  private String content;
  private int views;
  private int likeCount;



  public static BoardRsDto toDto(Board board) {
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    return boardRsDto;
  }
}
