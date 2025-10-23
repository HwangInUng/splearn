package study.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import study.splearn.domain.BaseEntity;
import study.splearn.domain.shared.Email;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	@NaturalId
	private Email email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private MemberDetail detail;

	public static Member register (MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
		Member member = new Member();

		member.email = new Email(registerRequest.email());
		member.nickname = requireNonNull(registerRequest.nickname());
		member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.passwordHash()));
		member.status = MemberStatus.PENDING;

		member.detail = MemberDetail.create();

		return member;
	}

	public void activate () {
		state(status == MemberStatus.PENDING, "대기 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
		this.detail.activatedAt();
	}

	public void deactivate () {
		state(status == MemberStatus.ACTIVE, "활성 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
		this.detail.deactivate();
	}

	public boolean verifyPassword (String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, this.passwordHash);
	}

	public void changeNickname (String nickname) {
		this.nickname = requireNonNull(nickname);
	}

	public void updateInfo(MemberInfoUpdateReqeust updateReqeust) {
		this.nickname = requireNonNull(updateReqeust.nickname());

		this.detail.updateInfo(updateReqeust);
	};

	public void changePassword (String password, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(requireNonNull(password));
	}

	public boolean isActive () {
		return this.status == MemberStatus.ACTIVE;
	}
}
