package JHboard.project.domain.like.entity;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes") //like는 예약어임
public class Like {

  @Id @GeneratedValue
  @Column(name = "likes_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)  //원래는 lazy이나 게시글을 가져올때 보통 멤버를 무조건 가져오기 때문에 eager로 설정해봄
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY) //댓글은 게시글 상세페이지에서만 필요하기 때문에 lazy
  @JoinColumn(name = "board_id")
  private Board board;

  public Like(Member member, Board board) {
    this.member = member;
    this.board = board;
  }
}
