package JHboard.project.domain.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRqDto {

  @NotNull(message = "사용하실 닉네임을 입력해 주세요.")
  private String nickname;

  @NotNull(message = "사용하실 아이디를 입력해 주세요.")
  private String username;

  @NotNull(message = "사용하실 비밀번호를 입력해주세요.")
  @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
  private String password;

  @NotNull(message = "비밀번호 확인을 위해 입력해주세요.")
  private String passwordConfirm;

}
