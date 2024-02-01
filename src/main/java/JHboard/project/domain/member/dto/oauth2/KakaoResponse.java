package JHboard.project.domain.member.dto.oauth2;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KakaoResponse implements OAuth2Response {


  private final Map<String, Object> attribute;
  private Map<String, Object> attributesAccount;
  private Map<String, Object> attributesProfile;

  public KakaoResponse(Map<String, Object> attribute) {
    this.attribute = attribute;
    this.attributesAccount = (Map<String, Object>) attribute.get("kakao_account"); //properties로 바꿔도 되겠다.
    this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getProviderId() {

    for (Map.Entry<String, Object> entry : attribute.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      log.info("key={}", key);
      log.info("value={}", value);
      log.info("================");
    }

    return attribute.get("id").toString();
  }

  @Override
  public String getEmail() {
    return attribute.get("email").toString();
  }

  @Override
  public String getName() {
    return attributesProfile.get("nickname").toString();
  }
}
