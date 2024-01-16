package JHboard.project.domain.board.controller;


import JHboard.project.domain.board.service.BoardFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardFileController {
  private final BoardFileService boardFileService;

  @GetMapping("/boardFile/download/{boardFileId}")
  public ResponseEntity<UrlResource> downloadFile(@PathVariable("boardFileId") Long boardFileId) {
    log.info("boardFileId={}", boardFileId);
    return boardFileService.downloadFile(boardFileId);
  }
}
