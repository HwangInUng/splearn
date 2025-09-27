package study.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.splearn.application.provided.MemberRegister;
import study.splearn.application.required.EmailSender;
import study.splearn.application.required.MemberRepository;
import study.splearn.domain.Member;
import study.splearn.domain.MemberRegisterRequest;
import study.splearn.domain.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register (MemberRegisterRequest registerRequest) {
		// check logic

		Member member = Member.register(registerRequest, passwordEncoder);

		memberRepository.save(member);
		emailSender.send(
				member.getEmail(),
				"등록을 완료해주세요.",
				"아래 링크를 클릭하여 등록을 완료해주세요."
		);

		return member;
	}
}
