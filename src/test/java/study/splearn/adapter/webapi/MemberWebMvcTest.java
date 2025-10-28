package study.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import study.splearn.application.member.provided.MemberRegister;
import study.splearn.domain.member.Member;
import study.splearn.domain.member.MemberFixture;
import study.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberWebMvcTest {
	// spring 6.2에서 등장.
	// static 메서드 테스트에 대한 부분을 간소화 가능
	// AssertJ와 접목되어 있기 때문에 편리한 기능을 제공
	final MockMvcTester mvcTester;
	final ObjectMapper objectMapper;

	// record에는 필드 추가가 불가능하기 때문에 @Autowired를 사용
	@MockitoBean
	MemberRegister memberRegister;


	@Test
	void register () throws JsonProcessingException {
		Member member = MemberFixture.createMember(1L);
		when(memberRegister.register(any())).thenReturn(member);

		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		assertThat(
				mvcTester.post()
						 .uri("/api/members")
						 .contentType(MediaType.APPLICATION_JSON)
						 .content(requestJson)
		).hasStatusOk()
		 .bodyJson()
		 .extractingPath("$.memberId")
		 .asNumber()
		 .isEqualTo(1);

		verify(memberRegister).register(request);
	}

	@Test
	void registerFail () throws JsonProcessingException {
		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");
		String requestJson = objectMapper.writeValueAsString(request);

		assertThat(
				mvcTester.post()
						 .uri("/api/members")
						 .contentType(MediaType.APPLICATION_JSON)
						 .content(requestJson)
		).hasStatus(HttpStatus.BAD_REQUEST);
	}
}