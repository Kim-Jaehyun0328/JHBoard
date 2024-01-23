package JHboard.project.domain.comment.repository;

import JHboard.project.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByBoardId(Long boardId);

}
