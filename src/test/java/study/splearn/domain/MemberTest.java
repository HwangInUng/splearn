package study.splearn.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.splearn.domain.MemberFixture.*;

class MemberTest {
	Member member;
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp () {
		this.passwordEncoder = createPasswordEncoder();
		this.member = Member.register(createMemberRegisterRequest(),
				passwordEncoder
		);
	}

	@Test
	void activate () {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail () {
		member.activate();

		assertThatThrownBy(member::activate)
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivate () {
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	void deactivateFail () {
		assertThatThrownBy(member::deactivate)
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void verifyPassword () {
		assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("invalid-password", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname () {
		assertThat(member.getNickname()).isEqualTo("nickname");

		member.changeNickname("potato");

		assertThat(member.getNickname()).isEqualTo("potato");
	}

	@Test
	void changePassword () {
		member.changePassword("very-secret", passwordEncoder);

		assertThat(member.verifyPassword("very-secret", passwordEncoder)).isTrue();
	}

	@Test
	void isActive () {
		assertThat(member.isActive()).isFalse();

		member.activate();

		assertThat(member.isActive()).isTrue();

		member.deactivate();

		assertThat(member.isActive()).isFalse();
	}

	@Test
	void invalidEmail () {
		assertThatThrownBy(
				() -> Member.register(
						createMemberRegisterReqeust("invalidEmail"),
						passwordEncoder
				)
		).isInstanceOf(Exception.class);
	}

}