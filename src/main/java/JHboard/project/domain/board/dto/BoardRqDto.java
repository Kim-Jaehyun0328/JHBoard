package JHboard.project.domain.board.dto;

import JHboard.project.domain.board.entity.Board;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class BoardRqDto {

  @NotNull
  private String title;

  @NotNull
  private String content;

  private List<MultipartFile> multipartFileList = new ArrayList<>();
  

  public BoardRqDto(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
