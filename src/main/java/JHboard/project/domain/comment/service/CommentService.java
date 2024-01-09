package JHboard.project.domain.comment.service;

import JHboard.project.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentService {
  Optional<Comment> findById(Long commentId);

  List<Comment> findAll();

  Comment create(Comment comment);

  void delete(Long commentId);
}
