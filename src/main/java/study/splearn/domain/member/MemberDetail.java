package study.splearn.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import study.splearn.domain.BaseEntity;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends BaseEntity {
	@Embedded
	private Profile profile;

	private String introduction;

	private LocalDateTime registeredAt;

	private LocalDateTime activatedAt;

	private LocalDateTime deactivatedAt;

	static MemberDetail create () {
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.registeredAt = LocalDateTime.now();

		return memberDetail;
	}

	void activatedAt () {
		isTrue(activatedAt == null, "이미 activatedAt이 설정되었습니다.");

		this.activatedAt = LocalDateTime.now();
	}

	void deactivate () {
		isTrue(deactivatedAt == null, "이미 deactivatedAt이 설정되었습니다.");

		this.deactivatedAt = LocalDateTime.now();
	}

	public void updateInfo (MemberInfoUpdateRequest updateReqeust) {
		this.profile = new Profile(updateReqeust.profileAddress());
		this.introduction = requireNonNull(updateReqeust.introduction());
	}
}
