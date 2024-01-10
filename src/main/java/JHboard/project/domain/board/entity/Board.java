package JHboard.project.domain.board.entity;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.member.entity.Member;
import JHboard.project.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "board_id")
  private Long id;

  private String title;
  private String content;

  private int views = 0;
  private int likeCount = 0;

  @ManyToOne(fetch = FetchType.EAGER)  //원래는 lazy이나 게시글을 가져올때 보통 멤버를 무조건 가져오기 때문에 eager로 설정해봄
  @JoinColumn(name = "member_id")
  private Member member;

  public static Board createEntity(BoardRqDto boardRqDto){
    Board board = new Board();
    board.title = boardRqDto.getTitle();
    board.content = boardRqDto.getContent();

    return board;
  }

  public void updateBoard(BoardRqDto boardRqDto) {
    this.title = boardRqDto.getTitle();
    this.content = boardRqDto.getContent();
  }

  public void updateView(){
    this.views += 1;
  }


}
