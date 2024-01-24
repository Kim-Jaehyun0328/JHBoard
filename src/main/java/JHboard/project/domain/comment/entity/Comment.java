package JHboard.project.domain.comment.entity;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "comment_id")
  private Long id;

  @Column(nullable = false, length = 1000)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)  //원래는 lazy이나 게시글을 가져올때 보통 멤버를 무조건 가져오기 때문에 eager로 설정해봄
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY) //댓글은 게시글 상세페이지에서만 필요하기 때문에 lazy
  @JoinColumn(name = "board_id")
  private Board board;

  @OneToMany(mappedBy = "parent", orphanRemoval = true)
  private List<Comment> children = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;


  public static Comment toEntity(Member member, Board board, String content){
    Comment comment = new Comment();

    comment.member = member;
    comment.board = board;
    comment.content = content;

    return comment;
  }

  public void updateComment(String updateContent){
    this.content = updateContent;
  }


}
