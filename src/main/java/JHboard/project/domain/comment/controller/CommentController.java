package JHboard.project.domain.comment.controller;

import JHboard.project.domain.comment.dto.CommentRqDto;
import JHboard.project.domain.comment.service.CommentService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String writeComment(@PathVariable("boardId") Long boardId, @ModelAttribute("commentForm") CommentRqDto comment, Principal principal) {
    if(principal == null) {
      //못 달게 해야 해
    }
    commentService.create(boardId, comment.getContent(), principal);

    return "redirect:/board/{boardId}";
  }

  /**
   * 댓글에 달린 답글을 위한 메소드
   * @param boardId
   * @param commentId
   * @param content
   * @param principal
   */
  @PostMapping("/comment/{boardId}/{commentId}")
  public String writeChildComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
      @RequestParam("content") String content, Principal principal) {
    log.info("content={}", content);
    commentService.createChildComment(boardId, commentId, content, principal);

    return "redirect:/board/{boardId}";
  }


  @PostMapping("/comment/{boardId}/{commentId}/edit")
  public String editComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
      @ModelAttribute("commentForm") CommentRqDto comment, Principal principal){
    commentService.updateComment(boardId, commentId, comment.getContent(), principal);
    return "redirect:/board/{boardId}";
  }

  @DeleteMapping("/comment/{boardId}/{commentId}/delete")
  public String deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
      Principal principal){
    commentService.delete(commentId, principal);
    return "redirect:/board/{boardId}";
  }


}
