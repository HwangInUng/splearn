package study.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.splearn.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static study.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static study.splearn.domain.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void createMember () {
		Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		assertThat(member.getId()).isNull();

		memberRepository.save(member);
		entityManager.flush();

		assertThat(member.getId()).isNotNull();
	}
}