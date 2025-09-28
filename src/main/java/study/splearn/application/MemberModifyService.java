package study.splearn.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.splearn.application.provided.MemberFinder;
import study.splearn.application.provided.MemberRegister;
import study.splearn.application.required.EmailSender;
import study.splearn.application.required.MemberRepository;
import study.splearn.domain.*;

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
