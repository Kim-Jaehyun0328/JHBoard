package JHboard.project.domain.like.repository;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.like.entity.Like;
import JHboard.project.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
  boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

  Optional<Like> findByBoardIdAndMemberId(Long boardId, Long memberId);
}
