package study.splearn.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
	Member member;
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp () {
		this.passwordEncoder = new PasswordEncoder() {
			@Override
			public String encode (String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches (String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};
		this.member = Member.create(new MemberCreateReqeust(
						"test@test.com",
						"test-nickname",
						"original-password"),
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
		assertThat(member.verifyPassword("original-password", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("invalid-password", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname () {
		assertThat(member.getNickname()).isEqualTo("test-nickname");

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
				() -> Member.create(
						new MemberCreateReqeust("invalidEmail", "nickname", "secret"),
						passwordEncoder
				)
		).isInstanceOf(Exception.class);
	}
}