package study.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;
import study.splearn.adapter.webapi.dto.MemberRegisterResponse;
import study.splearn.application.member.provided.MemberRegister;
import study.splearn.application.member.required.MemberRepository;
import study.splearn.domain.member.Member;
import study.splearn.domain.member.MemberFixture;
import study.splearn.domain.member.MemberRegisterRequest;
import study.splearn.domain.member.MemberStatus;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static study.splearn.AssertThatUtils.equalsTo;
import static study.splearn.AssertThatUtils.notNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class MemberApiTest {
	/*
	 * @Transactional 사용 시 주의
	 *
	 * Propagation의 REQUIRED_NEW, NESTED 사용 시
	 * 새로 만든 트랜잭션의 롤백이 수행되지 않아 수동으로 처리해야 함
	 *
	 * 동시성을 직접 테스트하는 작업 시 어노테이션으로 롤백이 되지 않는다.
	 * */
	final MockMvcTester mvcTester;
	final ObjectMapper objectMapper;
	final MemberRepository memberRepository;
	final MemberRegister memberRegister;


	@Test
	void register () throws JsonProcessingException, UnsupportedEncodingException {
		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		MvcTestResult result = mvcTester.post()
										.uri("/api/members")
										.contentType(MediaType.APPLICATION_JSON)
										.content(requestJson).exchange();

		assertThat(result)
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$.memberId", notNull())
				.hasPathSatisfying("$.email", equalsTo(request));

		MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

		Member member = memberRepository.findById(response.memberId()).orElseThrow();

		assertThat(member.getEmail().address()).isEqualTo(request.email());
		assertThat(member.getNickname()).isEqualTo(request.nickname());
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmail () throws JsonProcessingException {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		MvcTestResult result = mvcTester.post()
										.uri("/api/members")
										.contentType(MediaType.APPLICATION_JSON)
										.content(requestJson)
										.exchange();

		assertThat(result).apply(print()).hasStatus(HttpStatus.CONFLICT);
	}
}
