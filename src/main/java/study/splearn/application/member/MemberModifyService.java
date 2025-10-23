package study.splearn.application.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.splearn.application.member.provided.MemberFinder;
import study.splearn.application.member.provided.MemberRegister;
import study.splearn.application.member.required.EmailSender;
import study.splearn.application.member.required.MemberRepository;
import study.splearn.domain.member.*;
import study.splearn.domain.shared.Email;

@Service
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
	private final MemberFinder memberFinder;
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Member register (@Valid MemberRegisterRequest registerRequest) {
		checkDuplicateEmail(registerRequest.email());

		Member member = Member.register(registerRequest, passwordEncoder);

		memberRepository.save(member);
		sendRegistEmail(member);

		return member;
	}

	@Override
	public Member activate (Long memberId) {
		Member member = memberFinder.find(memberId);

		member.activate();

		return memberRepository.save(member);
	}

	@Override
	public Member deactivate (Long memberId) {
		Member member = memberFinder.find(memberId);

		member.deactivate();

		return memberRepository.save(member);
	}

	@Override
	public Member updateInfo (Long memberId, MemberInfoUpdateRequest updateRequest) {
		Member member = memberFinder.find(memberId);

		checkDuplicateProfile(member, updateRequest.profileAddress());

		member.updateInfo(updateRequest);
		return memberRepository.save(member);
	}

	private void checkDuplicateProfile (Member member, String profileAddress) {
		if(profileAddress.isEmpty()) return;

		Profile currentProfile = member.getDetail().getProfile();
		if(currentProfile != null && member.getDetail().getProfile().address().equals(profileAddress)) return;

		if(memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
			throw new DuplicateProfileException("이미 존재하는 프로필 주소입니다. :: " + profileAddress);
		}
	}

	private void sendRegistEmail (Member member) {
		emailSender.send(
				member.getEmail(),
				"등록을 완료해주세요.",
				"아래 링크를 클릭하여 등록을 완료해주세요."
		);
	}

	private void checkDuplicateEmail (String email) {
		if (memberRepository.findByEmail(new Email(email)).isPresent()) {
			throw new DuplicateEmailException("이미 존재하는 이메일입니다. :: " + email);
		}
	}


}
