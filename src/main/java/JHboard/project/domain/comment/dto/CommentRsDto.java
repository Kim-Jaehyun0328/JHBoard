package JHboard.project.domain.comment.dto;

import JHboard.project.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CommentRsDto {
  private Long id;
  private String content;
  private String writer;
  private String username;
  private CommentRsDto parent;
  private LocalDateTime modifiedAt;
  private List<CommentRsDto> children = new ArrayList<>();


  public static CommentRsDto toDto(Comment comment) {
    CommentRsDto commentRsDto = new CommentRsDto();
    commentRsDto.id = comment.getId();
    commentRsDto.content = comment.getContent();
    commentRsDto.writer = comment.getMember().getNickname();
    commentRsDto.username = comment.getMember().getUsername();
    commentRsDto.modifiedAt = comment.getModifiedAt();


    if (comment.getChildren().size() > 0) {
      commentRsDto.children = comment.getChildren().stream()
          .map(c -> toDto(c))
          .collect(Collectors.toList());
    }

    return commentRsDto;
  }

}
