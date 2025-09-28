package study.splearn.application.provided;

import study.splearn.domain.Member;

/**
 * 회원을 조회한다.
 */
public interface MemberFinder {
	Member find(Long memberId);
}
