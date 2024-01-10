package JHboard.project.domain.board.dto;

import JHboard.project.domain.board.entity.Board;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRqDto {

  private Long id;

  @NotNull
  private String title;

  @NotNull
  private String content;



  public BoardRqDto(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
