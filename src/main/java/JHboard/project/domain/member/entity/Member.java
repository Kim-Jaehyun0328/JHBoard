package JHboard.project.domain.member.entity;

import JHboard.project.domain.member.dto.RegisterRqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String nickname;
  private String username; //아이디
  private String password; //비밀번호


  public static Member createDto(RegisterRqDto registerRqDto) {
    Member member = new Member();
    member.nickname = registerRqDto.getNickname();
    member.username = registerRqDto.getUsername();
    member.password = registerRqDto.getPassword();

    return member;
  }

}
