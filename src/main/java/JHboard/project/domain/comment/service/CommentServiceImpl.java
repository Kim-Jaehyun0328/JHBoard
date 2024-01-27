package JHboard.project.domain.comment.service;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.repository.CommentRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import com.amazonaws.services.kms.model.NotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
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

  @Override
  public List<Comment> findAllByBoardId(Long boardId) {
    return commentRepository.findByBoardId(boardId);
  }




  @Transactional
  @Override
  public void updateComment(Long boardId, Long commentId, String updateContent, Principal principal) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("옳지 않은 접근입니다."));

    if(!comment.getMember().getUsername().equals(principal.getName())){
      throw new NotFoundException("옳지 않은 접근입니다.");
    }
    comment.updateComment(updateContent);
  }

  @Transactional
  @Override
  public Comment create(Long boardId, String content, Principal principal) {
    String username = principal.getName();
    Member member = memberService.findByUsername(username)
        .orElseThrow(() -> new NotFoundException("옳지 않은 유저입니다."));
    Board board = boardService.findById(boardId)
        .orElseThrow(() -> new NotFoundException("옳지 않은 게시판입니다."));

    Comment comment = Comment.toEntity(member, board, content);
    board.updateCommentCount(1);

    return commentRepository.save(comment);
  }

  @Override
  @Transactional
  public Comment createChildComment(Long boardId, Long commentId, String content, Principal principal) {
    String username = principal.getName();
    Member member = memberService.findByUsername(username)
        .orElseThrow(() -> new NotFoundException("옳지 않은 유저입니다."));
    Board board = boardService.findById(boardId)
        .orElseThrow(() -> new NotFoundException("옳지 않은 게시판입니다."));
    Comment parentComment = findById(commentId)
        .orElseThrow(() -> new NotFoundException("옳지 않은 댓글입니다."));

    Comment comment = Comment.toEntity(member, board, content);
    comment.connetParentComment(parentComment);
    parentComment.getChildren().add(comment);
    board.updateCommentCount(1);

    return commentRepository.save(comment);
  }

  @Transactional
  @Override
  public void delete(Long commentId, Principal principal) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException("옳지 않은 접근입니다."));

    if(!comment.getMember().getUsername().equals(principal.getName())){
      throw new NotFoundException("옳지 않은 접근입니다.");
    }

    //자식 댓글(답글이라면)
    if(comment.getParent() != null) {
      comment.getBoard().updateCommentCount(-1);
    }else{
      log.info("==================================");
      comment.getBoard().updateCommentCount((-1)*(comment.getChildren().size()+1));
      log.info("==================================");
    }
    commentRepository.deleteById(commentId);
  }
}
