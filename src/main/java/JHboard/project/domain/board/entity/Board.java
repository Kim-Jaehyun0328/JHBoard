package JHboard.project.domain.board.entity;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.like.entity.Like;
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
public class Board extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "board_id")
  private Long id;

  private String title;

  @Column(length = 1000)
  private String content;

  private int views = 0;
  private int likeCount = 0;

  @ManyToOne(fetch = FetchType.LAZY)  //원래는 lazy이나 게시글을 가져올때 보통 멤버를 무조건 가져오기 때문에 eager로 설정해봄
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "board")
  private List<BoardFile> boardFiles = new ArrayList<>();

  @OneToMany(mappedBy = "board")
  private List<Like> likes = new ArrayList<>();

  public static Board createEntity(BoardRqDto boardRqDto, Member member, List<BoardFile> boardFiles){
    Board board = new Board();
    board.title = boardRqDto.getTitle();
    board.content = boardRqDto.getContent();
    board.member = member;
    board.boardFiles = boardFiles;
    return board;
  }

  public void updateBoard(BoardRqDto boardRqDto, List<BoardFile> boardFiles) {
    this.title = boardRqDto.getTitle();
    this.content = boardRqDto.getContent();
    this.boardFiles = boardFiles;
  }

  public void updateView(){
    this.views += 1;
  }

  public void updateLikeCount(int num) {
    this.likeCount += num;
  }


}
