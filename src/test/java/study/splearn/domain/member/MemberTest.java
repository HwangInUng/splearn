package study.splearn.domain.member;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.splearn.domain.member.MemberFixture.*;

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
	void registerMEmber () {
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(member.getDetail().getRegisteredAt()).isNotNull();
	}

	@Test
	void activate () {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
		assertThat(member.getDetail().getActivatedAt()).isNotNull();
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
		assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
	}

	@Test
	void deactivateFail () {
		assertThatThrownBy(member::deactivate)
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void verifyPassword () {
		assertThat(member.verifyPassword("long-secret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("invalid-password", passwordEncoder)).isFalse();
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

	@Test
	void updateInfo () {
		member.activate();

		var request = new MemberInfoUpdateRequest("iwhwang", "iwhwang100", "테스트 소개");
		member.updateInfo(request);

		assertThat(member.getNickname()).isEqualTo(request.nickname());
		assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
		assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());

	}

	@Test
	void updateInfoFail () {
		var request = new MemberInfoUpdateRequest("iwhwang", "iwhwang100", "테스트 소개");

	    assertThatThrownBy(
				() -> member.updateInfo(request)
		).isInstanceOf(IllegalStateException.class);
	}
}