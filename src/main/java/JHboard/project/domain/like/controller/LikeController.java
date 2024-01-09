package JHboard.project.domain.like.controller;

import JHboard.project.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LikeController {
  private final LikeService likeService;
}
