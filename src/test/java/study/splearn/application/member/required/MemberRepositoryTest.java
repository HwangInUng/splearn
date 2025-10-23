package study.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import study.splearn.domain.member.Member;
import study.splearn.domain.member.MemberStatus;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static study.splearn.domain.member.MemberFixture.createPasswordEncoder;

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

		assertThat(member.getId()).isNotNull();

		entityManager.flush();
		entityManager.clear();

		var found = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(found.getDetail().getRegisteredAt()).isNotNull();
	}

	@Test
	void duplicateEmailFaile () {
		Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		memberRepository.save(member);

		Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
		assertThatThrownBy(
				() -> memberRepository.save(member2)
		).isInstanceOf(DataIntegrityViolationException.class);
	}
}