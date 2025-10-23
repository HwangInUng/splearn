package study.splearn.domain.member;

public record MemberInfoUpdateReqeust(
		String nickname,
		String profileAddress,
		String introduction
) {
}
