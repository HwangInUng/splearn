package study.splearn.application.required;

import org.springframework.data.repository.Repository;
import study.splearn.domain.Email;
import study.splearn.domain.Member;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
	Member save (Member member);

	Optional<Member> findByEmail (Email email);
}
