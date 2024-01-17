package JHboard.project.domain.board.service;


import JHboard.project.global.s3.S3Config;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final S3Config s3Config;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucket;

  private String localLocation = "/Users/gimjaehyeon/localImage/";

  public String imageUpload(MultipartRequest request) throws IOException {
    MultipartFile file = request.getFile("upload");

    String filename = file.getOriginalFilename();
    String ext = filename.substring(filename.indexOf("."));

    String uuidFileName = UUID.randomUUID() + ext;
    String localPath = localLocation + uuidFileName;

    File localFile = new File(localPath);
    file.transferTo(localFile);

    s3Config.s3Builder().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(
        CannedAccessControlList.PublicRead));
    String s3Url = s3Config.s3Builder().getUrl(bucket, uuidFileName).toString();

    localFile.delete();

    return s3Url;
  }
}
