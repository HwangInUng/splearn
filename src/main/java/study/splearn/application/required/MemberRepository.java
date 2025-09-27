package study.splearn.application.required;

import org.springframework.data.repository.Repository;
import study.splearn.domain.Member;

public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);
}
