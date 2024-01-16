package JHboard.project.domain.board.dto;

import JHboard.project.domain.board.entity.BoardFile;
import lombok.Data;

@Data
public class BoardFileDto {
  private Long id;
  private String originalFileName;
  private String uniqueFileName;
  private String savedFileName;

  public BoardFileDto(BoardFile boardFile) {
    this.id = boardFile.getId();
    this.originalFileName = boardFile.getOriginFileName();
    this.uniqueFileName = boardFile.getUniqueFileName();
    this.savedFileName = boardFile.getSavedFileName();
  }

}
