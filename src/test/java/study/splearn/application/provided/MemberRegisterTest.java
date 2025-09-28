package study.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.splearn.SplearnTestConfiguration;
import study.splearn.domain.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
		MemberRegister memberRegister,
		EntityManager entityManager
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

	@Test
	void memberRegisterRequestFail () {
		extracted(new MemberRegisterRequest("test@test.com", "nic", "longsecret"));
		extracted(new MemberRegisterRequest("test@test.com", "t".repeat(21), "longsecret"));
	}

	@Test
	void activate () {
	    Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		entityManager.flush();
		entityManager.clear();

		member = memberRegister.activate(member.getId());

		entityManager.flush();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	private void extracted (MemberRegisterRequest invalidRequest) {
		assertThatThrownBy(
				() -> memberRegister.register(invalidRequest)
		).isInstanceOf(ConstraintViolationException.class);
	}
}