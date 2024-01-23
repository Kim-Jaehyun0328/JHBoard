package JHboard.project.domain.comment.service;

import JHboard.project.domain.comment.entity.Comment;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CommentService {
  Optional<Comment> findById(Long commentId);

  List<Comment> findAll();

  Comment create(Long boardId, String content, Principal principal);

  void delete(Long commentId);

  List<Comment> findAllByBoardId(Long boardId);
}
