package JHboard.project.domain.like.service;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.service.BoardService;
import JHboard.project.domain.like.entity.Like;
import JHboard.project.domain.like.repository.LikeRepository;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.domain.member.repository.MemberRepository;
import JHboard.project.domain.member.service.MemberService;
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
public class LikeServiceImpl implements LikeService{
  private final LikeRepository likeRepository;
  private final BoardService boardService;
//  private final MemberService memberService;
  private final MemberRepository memberRepository;

  @Override
  public Optional<Like> findById(Long likeId) {
    return likeRepository.findById(likeId);
  }

  @Override
  public List<Like> findAll() {
    return likeRepository.findAll();
  }

  @Override
  public Like create(Like like) {
    return likeRepository.save(like);
  }

  @Override
  public void delete(Long likeId) {
    likeRepository.deleteById(likeId);
  }


  @Override
  @Transactional
  public boolean saveLike(Long boardId, Long memberId) {
    if(!findLike(boardId, memberId)) {
      Member member = memberRepository.findById(memberId).orElseThrow(() ->
          new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
      Board board = boardService.findById(boardId).orElseThrow(() ->
          new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

      Like like = new Like(member, board);
      likeRepository.save(like);
      board.getLikes().add(like);
      board.updateLikeCount(1);

      return true;
    } else {
      Like like = likeRepository.findByBoardIdAndMemberId(boardId, memberId).get();
      Board board = boardService.findById(boardId).get();
      board.getLikes().remove(like);
      board.updateLikeCount(-1);
      likeRepository.delete(like);

      return false;
    }
  }

  @Override
  public boolean findLike(Long boardId, Long memberId) {
    return likeRepository.existsByBoardIdAndMemberId(boardId, memberId);
  }

}
