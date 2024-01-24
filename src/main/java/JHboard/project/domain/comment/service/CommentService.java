package JHboard.project.domain.comment.service;

import JHboard.project.domain.comment.dto.CommentRsDto;
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
   * Comment 엔티티 리스트를 db에서 가져온 뒤 dto로 변환 후에 반환
   * @param boardId
   * @return
   */
  List<CommentRsDto> getCommentsForBoardId(Long boardId);
}
