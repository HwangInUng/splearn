package study.splearn.application.provided;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import study.splearn.SplearnTestConfiguration;
import study.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
		MemberRegister memberRegister
) {
	@Test
	void register () {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmailFail () {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThatThrownBy(
				() -> memberRegister.register(MemberFixture.createMemberRegisterRequest())
		).isInstanceOf(DuplicateEmailException.class);
	}
}