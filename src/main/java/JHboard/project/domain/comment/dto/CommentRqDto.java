package JHboard.project.domain.comment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class CommentRqDto {
  private Long memberId;
  private Long parentId;
  private String content;



  public CommentRqDto(String content) {
    this.content = content;
  }
}
