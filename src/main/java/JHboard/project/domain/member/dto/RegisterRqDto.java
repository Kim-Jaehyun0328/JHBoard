package JHboard.project.domain.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRqDto {

  @NotNull(message = "사용하실 닉네임을 입력해 주세요.")
  private String nickname;

  @NotNull(message = "사용하실 아이디를 입력해 주세요.")
  private String username;

  @NotNull(message = "사용하실 비밀번호를 입력해주세요.")
  private String password;

  @NotNull(message = "비밀번호 확인을 위해 입력해주세요.")
  private String passwordConfirm;



}
