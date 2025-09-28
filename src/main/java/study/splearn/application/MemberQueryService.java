package study.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.splearn.application.provided.MemberFinder;
import study.splearn.application.required.MemberRepository;
import study.splearn.domain.Member;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
	private final MemberRepository memberRepository;

	@Override
	public Member find (Long memberId) {
		return memberRepository.findById(memberId)
							   .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. :: " + memberId));
	}
}
