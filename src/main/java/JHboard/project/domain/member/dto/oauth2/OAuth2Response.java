package JHboard.project.domain.member.dto.oauth2;

public interface OAuth2Response {

  //제공자 (ex: naver, google,...)
  String getProvider();

  //제공자로부터 발급해주는 아이디(번호)
  String getProviderId();

  String getEmail();

  //사용자 설명(설정한 이름)
  String getName();
}
