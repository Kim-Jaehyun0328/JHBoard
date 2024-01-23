package JHboard.project.domain.comment.dto;

import JHboard.project.domain.comment.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CommentRsDto {
  private Long id;
  private String content;
  private String writer;
  private List<CommentRsDto> children = new ArrayList<>();


  public static CommentRsDto toDto(Comment comment) {
    CommentRsDto commentRsDto = new CommentRsDto();
    commentRsDto.id = comment.getId();
    commentRsDto.content = comment.getContent();
    commentRsDto.writer = comment.getMember().getNickname();

    return commentRsDto;
  }


}
