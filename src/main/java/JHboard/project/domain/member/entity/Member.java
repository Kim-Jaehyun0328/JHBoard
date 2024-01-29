package JHboard.project.domain.member.entity;

import static jakarta.persistence.EnumType.*;

import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.comment.entity.Comment;
import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "nickname", "username"})
public class Member extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  @Column(unique = true)
  private String nickname;

  @Column(unique = true)
  private String username; //아이디
  private String password; //비밀번호

  @Enumerated(STRING)
  private MemberRole memberRole;

  @OneToMany(mappedBy = "member")
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Comment> comments = new ArrayList<>();


  public static Member createEntity(RegisterRqDto registerRqDto, PasswordEncoder passwordEncoder) {
    Member member = new Member();
    member.nickname = registerRqDto.getNickname();
    member.username = registerRqDto.getUsername();
    member.password = passwordEncoder.encode(registerRqDto.getPassword());
    member.memberRole = MemberRole.ROLE_MEMBER;

    return member;
  }

  public static Member createEntity(RegisterRqDto registerRqDto) {
    Member member = new Member();
    member.nickname = registerRqDto.getNickname();
    member.username = registerRqDto.getUsername();
    member.password = registerRqDto.getPassword();
    member.memberRole = MemberRole.ROLE_MEMBER;

    return member;
  }

}
