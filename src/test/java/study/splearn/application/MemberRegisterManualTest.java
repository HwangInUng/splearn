package study.splearn.application;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.yaml.snakeyaml.emitter.Emitable;
import study.splearn.application.provided.MemberRegister;
import study.splearn.application.required.EmailSender;
import study.splearn.application.required.MemberRepository;
import study.splearn.domain.Email;
import study.splearn.domain.Member;
import study.splearn.domain.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static study.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static study.splearn.domain.MemberFixture.createPasswordEncoder;

class MemberRegisterManualTest {
	@Test
	void registerTestStub () {
		MemberRegister memberRegister = new MemberService(
				new MemberRepositoryStub(),
				new EmailSenderStub(),
				createPasswordEncoder()
		);

		Member member = memberRegister.register(createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void registerTestMock () {
		EmailSenderMock emailSenderMock = new EmailSenderMock();

		MemberRegister memberRegister = new MemberService(
				new MemberRepositoryStub(),
				emailSenderMock,
				createPasswordEncoder()
		);

		Member member = memberRegister.register(createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		assertThat(emailSenderMock.getTos()).hasSize(1);
		assertThat(emailSenderMock.getTos().get(0)).isEqualTo(member.getEmail());
	}

	@Test
	void registerTestMockito () {
		EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
	    MemberRegister memberRegister = new MemberService(
				new MemberRepositoryStub(),
				emailSenderMock,
				createPasswordEncoder()
		);

		Member member = memberRegister.register(createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
	}

	static class MemberRepositoryStub implements MemberRepository {
		@Override
		public Member save (Member member) {
			ReflectionTestUtils.setField(member, "id", 1L);
			return member;
		}

		@Override
		public Optional<Member> findByEmail (Email email) {
			return Optional.empty();
		}
	}

	static class EmailSenderStub implements EmailSender {
		@Override
		public void send (Email email, String subject, String body) {
		}
	}

	static class EmailSenderMock implements EmailSender {
		List<Email> tos = new ArrayList<>();

		public List<Email> getTos () {
			return tos;
		}

		@Override
		public void send (Email email, String subject, String body) {
			tos.add(email);
		}
	}
}