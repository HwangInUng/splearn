package study.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.splearn.adapter.webapi.dto.MemberRegisterResponse;
import study.splearn.application.member.provided.MemberRegister;
import study.splearn.domain.member.Member;
import study.splearn.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {
	private final MemberRegister memberRegister;

	@PostMapping("/api/members")
	public MemberRegisterResponse register (@RequestBody @Valid MemberRegisterRequest request) {
		Member member = memberRegister.register(request);

		return MemberRegisterResponse.of(member);
	}
}
