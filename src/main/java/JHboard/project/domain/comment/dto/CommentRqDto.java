package JHboard.project.domain.comment.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CommentRqDto {
  private Long memberId;
  private Long parentId;
  private String content;

  public CommentRqDto(String content) {
    this.content = content;
  }
}
