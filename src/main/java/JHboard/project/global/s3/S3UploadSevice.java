package JHboard.project.global.s3;

import JHboard.project.domain.board.entity.BoardFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadSevice {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucket;

  /**
   * 파일 업로드 구현
   * @param multipartFile
   * @return url
   * @throws IOException
   */
  public List<BoardFile> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
    List<BoardFile> boardFiles = new ArrayList<>();
    for (MultipartFile multipartFile : multipartFiles) {

      if(!multipartFile.isEmpty()){
        boardFiles.add(saveFile(multipartFile));
      }
    }
    return boardFiles;
  }

  private BoardFile saveFile(MultipartFile multipartFile) throws IOException {
    String originalFilename = multipartFile.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());
    String uniqueFileName = createStoreFileName(originalFilename);  //랜덤값 + 확장자
    amazonS3.putObject(bucket, uniqueFileName, multipartFile.getInputStream(), metadata);
    return new BoardFile(originalFilename, uniqueFileName, amazonS3.getUrl(bucket, originalFilename).toString());
  }


  public ResponseEntity<UrlResource> downloadFile(String uniqueFileName) {
    UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, uniqueFileName));
    String contentDisposition = "attachment; filename=\"" +  uniqueFileName + "\"";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
        .body(urlResource);
  }

  public void deleteFile(String originalFileName){
    amazonS3.deleteObject(bucket, originalFileName);
  }

  private String createStoreFileName(String originalFilename) {
    String ext = extractExt(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
}