package JHboard.project.domain.member.service;


import static org.assertj.core.api.Assertions.*;

import JHboard.project.domain.member.dto.RegisterRqDto;
import JHboard.project.domain.member.entity.Member;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
class MemberServiceImplTest {

  @Autowired
  MemberService memberService;

//  @Autowired
//  EntityManager em;
//  @Autowired
//  PasswordEncoder passwordEncoder;  //create 메소드에 있다.



  @Test
  @Transactional
  @DisplayName("회원가입 테스트")
  @Rollback(value = false)
  public void 회원가입(){
    //given
    RegisterRqDto registerRqDto = new RegisterRqDto();
    registerRqDto.setNickname("김재현");
    registerRqDto.setUsername("rlawwogus");
    registerRqDto.setPassword("123");
    Member member = memberService.create(registerRqDto);

    //when
    Member findMember = memberService.findById(member.getId()).get();

    //then
    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember).isEqualTo(member);
  }

  @Test
  @DisplayName("중복 회원가입 테스트")
  @Rollback(value = false)
  public void 중복회원() {
    //given
    RegisterRqDto registerRqDto = new RegisterRqDto();
    registerRqDto.setNickname("김재현");
    registerRqDto.setUsername("rlawwogus");
    registerRqDto.setPassword("123");
    memberService.create(registerRqDto);
    System.out.println("hello");

    RegisterRqDto registerRqDto2 = new RegisterRqDto();
    registerRqDto2.setNickname("안녕하세요");
    registerRqDto2.setUsername("rlawwogus");
    registerRqDto2.setPassword("123");

    RegisterRqDto registerRqDto3 = new RegisterRqDto();
    registerRqDto3.setNickname("김재현");
    registerRqDto3.setUsername("aaa");
    registerRqDto3.setPassword("123");

    //when
    Throwable e = org.junit.jupiter.api.Assertions.assertThrows( //username 중복 체크
        IllegalStateException.class, () -> {
          memberService.create(registerRqDto2);
        });

    Throwable e2 = org.junit.jupiter.api.Assertions.assertThrows( //nickname 중복 체크
        IllegalStateException.class, () -> {
          memberService.create(registerRqDto3);
        });

    //then
    Assertions.assertThat(e.getMessage()).isEqualTo("이미 가입된 회원입니다.");
    Assertions.assertThat(e2.getMessage()).isEqualTo("중복된 닉네임이 있습니다.");
  }
}