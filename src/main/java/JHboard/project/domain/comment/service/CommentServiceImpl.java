package JHboard.project.domain.comment.service;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.comment.dto.CommentRqDto;
import JHboard.project.domain.comment.dto.CommentRsDto;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.comment.repository.CommentRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.service.MemberService;
import com.amazonaws.services.kms.model.NotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    commentRepository.deleteById(commentId);
  }



  public void insert(Long boardId, CommentRqDto commentRqDto) {
    Member member = memberService.findById(commentRqDto.getMemberId())
        .orElseThrow(() -> new NotFoundException(
            "Could not found member id: " + commentRqDto.getMemberId()));
    boardService.findById(boardId)
        .orElseThrow(() -> new NotFoundException("Could not found board id: " + boardId));
  }


  @Override
  public List<CommentRsDto> getCommentsForBoardId(Long boardId) {
    List<Comment> comments = findAllByBoardId(boardId);

    return comments.stream()
        .map(c -> CommentRsDto.toDto(c)).collect(Collectors.toList());
  }
}
