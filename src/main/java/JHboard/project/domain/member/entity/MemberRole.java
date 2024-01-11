package JHboard.project.domain.member.entity;


import lombok.Getter;

@Getter
public enum MemberRole {  //앞에 ROLE_ 필수이다. 이렇게 안 하면 필터에서 인지 못 함
  ROLE_MEMBER, ROLE_ADMIN
}
