package JHboard.project.domain.comment.service;

import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.repository.CommentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
  private final CommentRepository commentRepository;

  @Override
  public Optional<Comment> findById(Long commentId) {
    return commentRepository.findById(commentId);
  }

  @Override
  public List<Comment> findAll() {
    return commentRepository.findAll();
  }

  @Override
  public Comment create(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public void delete(Long commentId) {
    commentRepository.deleteById(commentId);
  }
}
