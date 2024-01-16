package JHboard.project.domain.board.service;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.entity.BoardFile;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

public interface BoardFileService {
  void create(BoardFile boardFile);

  void delete(BoardFile boardFile);

  List<BoardFile> deleteAndCreateBoardFile(BoardRqDto boardRqDto, Board board)
      throws IOException;

  ResponseEntity<UrlResource> downloadFile(Long boardFileId);

}
