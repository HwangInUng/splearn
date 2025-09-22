package study.splearn.domain;

import lombok.Getter;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Getter
public class Member {
	private String email;
	private String nickname;
	private String passwordHash;
	private MemberStatus status;

	public Member (String email, String nickname, String passwordHash) {
		this.email = requireNonNull(email);
		this.nickname = requireNonNull(nickname);
		this.passwordHash = requireNonNull(passwordHash);

		this.status = MemberStatus.PENDING;
	}

	public void activate () {
		state(status == MemberStatus.PENDING, "대기 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
	}

	public void deactivate () {
		state(status == MemberStatus.ACTIVE, "활성 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
	}
}
