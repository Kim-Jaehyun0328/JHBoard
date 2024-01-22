package JHboard.project.domain.comment.service;

import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.comment.dto.CommentRqDto;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.repository.CommentRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import com.amazonaws.services.kms.model.NotFoundException;
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
  private final MemberService memberService;
  private final BoardService boardService;

  @Override
  public Optional<Comment> findById(Long commentId) {
    return commentRepository.findById(commentId);
  }

  @Override
  public List<Comment> findAll() {
    return commentRepository.findAll();
  }

  @Transactional
  @Override
  public Comment create(Comment comment) {
    return commentRepository.save(comment);
  }

  @Transactional
  @Override
  public void delete(Long commentId) {
    commentRepository.deleteById(commentId);
  }



  public void insert(Long boardId, CommentRqDto commentRqDto) {
    Member member = memberService.findById(commentRqDto.getMemberId())
        .orElseThrow(() -> new NotFoundException(
            "Could not found member id: " + commentRqDto.getMemberId()));
    boardService.findById(boardId)
        .orElseThrow(() -> new NotFoundException("Could not found board id: " + boardId));
  }
}
