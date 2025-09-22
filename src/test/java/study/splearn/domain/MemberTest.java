package study.splearn.domain;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

	@Test
	void consturctorNullCheck () {

		assertThatThrownBy(
				() -> new Member(null, "test", "password-hash")
		).isInstanceOf(NullPointerException.class);
	}

	@Test
	void activate () {
		Member member = new Member("test@test.com", "test-name", "password-hash");

		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail () {
		Member member = new Member("test@test.com", "test-name", "password-hash");

		member.activate();

		assertThatThrownBy(member::activate)
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivate () {
		Member member = new Member("test@test.com", "test-name", "password-hash");
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	void deactivateFail () {
		Member member = new Member("test@test.com", "test-name", "password-hash");

		assertThatThrownBy(member::deactivate)
				.isInstanceOf(IllegalStateException.class);
	}
}