package JHboard.project.domain.like.service;

import JHboard.project.domain.like.entity.Like;
import JHboard.project.domain.like.repository.LikeRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
  private final LikeRepository likeRepository;

  @Override
  public Optional<Like> findById(Long likeId) {
    return likeRepository.findById(likeId);
  }

  @Override
  public List<Like> findAll() {
    return likeRepository.findAll();
  }

  @Override
  public Like create(Like like) {
    return likeRepository.save(like);
  }

  @Override
  public void delete(Long likeId) {
    likeRepository.deleteById(likeId);
  }
}
