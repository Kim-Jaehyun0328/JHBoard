package JHboard.project.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRqDto {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;
}
