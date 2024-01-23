package JHboard.project.domain.comment.controller;

import JHboard.project.domain.comment.dto.CommentRsDto;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.service.CommentService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 작성
   * @param boardId
   * @param principal
   */
  @PostMapping("/comment/{boardId}")
  public ResponseEntity<List<CommentRsDto>> writeComment(@PathVariable("boardId") Long boardId, @RequestBody String content, Principal principal) {
    if(principal == null) {
      //못 달게 해야 해
    }
    commentService.create(boardId, content, principal);
    List<Comment> comments = commentService.findAllByBoardId(boardId);
    List<CommentRsDto> commentRsDtos = comments.stream()
        .map(c -> CommentRsDto.toDto(c)).collect(Collectors.toList());
    return new ResponseEntity<>(commentRsDtos, HttpStatus.OK);
  }

  @PostMapping("/comment/{boardId}/{commentId}")
  public void writeChildComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
      @RequestParam("content") String content, Principal principal) {

  }
}
