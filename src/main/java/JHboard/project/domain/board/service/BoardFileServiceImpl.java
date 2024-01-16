package JHboard.project.domain.board.service;

import JHboard.project.domain.board.dto.BoardRqDto;
import JHboard.project.domain.board.entity.Board;
import JHboard.project.domain.board.entity.BoardFile;
import JHboard.project.domain.board.repository.BoardFileRepository;
import JHboard.project.global.s3.S3UploadSevice;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardFileServiceImpl implements BoardFileService{
  private final BoardFileRepository boardFileRepository;
  private final S3UploadSevice s3UploadSevice;

  @Override
  @Transactional
  public void create(BoardFile boardFile) {
    boardFileRepository.save(boardFile);
  }

  @Override
  public void delete(BoardFile boardFile) {
    boardFileRepository.deleteById(boardFile.getId()); //이것은 db에서 삭제
    s3UploadSevice.deleteFile(boardFile.getUniqueFileName()); //이것은 s3에서 삭제
  }

  /**
   * 게시판 업데이트할 때 사용되는 로직
   * 기존의 boardFile은 db, s3에서 삭제하고, 새롭게 받은 multipartfile을 boardFile 클래스로 반환한 뒤 다시 repository에 저장한다.
   * @param boardRqDto
   * @param board
   * @return
   * @throws IOException
   */
  @Override
  public List<BoardFile> deleteAndCreateBoardFile(
      BoardRqDto boardRqDto, Board board) throws IOException {
    for (BoardFile boardFile : board.getBoardFiles()) {  //db+s3에서 기존 boardFile 삭제하기
      delete(boardFile);
    }
    List<BoardFile> boardFiles = s3UploadSevice.saveFiles(boardRqDto.getMultipartFileList());
    for (BoardFile boardFile : boardFiles) {
      boardFile.connetBoardId(board);
      create(boardFile);
    }
    return boardFiles;
  }

  @Override
  public ResponseEntity<UrlResource> downloadFile(Long boardFileId) {
    BoardFile boardFile = boardFileRepository.findById(boardFileId)
        .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다. id=" + boardFileId));
    return s3UploadSevice.downloadFile(boardFile.getUniqueFileName());
  }
}
