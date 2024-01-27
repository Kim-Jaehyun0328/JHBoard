package JHboard.project.domain.comment.service;

import JHboard.project.domain.comment.entity.Comment;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CommentService {
  Optional<Comment> findById(Long commentId);

  List<Comment> findAll();


  Comment create(Long boardId, String content, Principal principal);

  void delete(Long commentId, Principal principal);

  List<Comment> findAllByBoardId(Long boardId);

  void updateComment(Long boardId, Long commentId, String content, Principal principal);




  /**
   * 자식 댓글(답글) 생성
   * @param boardId
   * @param commentId
   * @param content
   * @param principal
   * @return
   */
  Comment createChildComment(Long boardId, Long commentId, String content, Principal principal);
}
