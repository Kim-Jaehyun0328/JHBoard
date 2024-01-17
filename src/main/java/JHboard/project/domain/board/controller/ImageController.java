package JHboard.project.domain.board.controller;

import JHboard.project.domain.board.service.ImageService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

@Controller
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/image/upload")
  @ResponseBody
  public Map<String, Object> imageUpload(MultipartRequest request) {
    Map<String, Object> responseData = new HashMap<>();

    try{
      String s3Url = imageService.imageUpload(request);

      responseData.put("uploaded", true);
      responseData.put("url", s3Url);

      return responseData;
    } catch (IOException e) {

      responseData.put("uploaded", false);

      return responseData;
    }
  }
}
