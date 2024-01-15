package JHboard.project.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3UploadSevice {
  private final AmazonS3 amazonS3;

  @Value("cloud.aws.s3.bucketName")
  private String bucket;

  /**
   * 파일 업로드 구현
   * @param multipartFile
   * @return url
   * @throws IOException
   */
  public String saveFile(MultipartFile multipartFile) throws IOException {
    String originalFilename = multipartFile.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());

    amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
    return amazonS3.getUrl(bucket, originalFilename).toString();
  }

  public ResponseEntity<UrlResource> downloadImage(String originalFileName) {
    UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFileName));
    String contentDisposition = "attachment; filename=\"" +  originalFileName + "\"";

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
        .body(urlResource);
  }


}