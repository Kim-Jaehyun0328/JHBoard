package JHboard.project.domain.board.dto;


import JHboard.project.domain.board.entity.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class BoardRsDto {

  private Long id;
  private String title;
  private String content;
  private int views;
  private int likeCount;
  private List<BoardFileDto> boardFileDtoList = new ArrayList<>();




  public static BoardRsDto toDtoForList(Board board){
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    return boardRsDto;
  }

  public static BoardRsDto toDto(Board board) { //디테일
    BoardRsDto boardRsDto = new BoardRsDto();
    boardRsDto.id = board.getId();
    boardRsDto.title = board.getTitle();
    boardRsDto.content = board.getContent();
    boardRsDto.views = board.getViews();
    boardRsDto.likeCount = board.getLikeCount();
    boardRsDto.boardFileDtoList = board.getBoardFiles().stream()
        .map(bf -> new BoardFileDto(bf)).collect(Collectors.toList());
    return boardRsDto;
  }
}
