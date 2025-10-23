package study.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import study.splearn.application.member.required.EmailSender;
import study.splearn.domain.member.MemberFixture;
import study.splearn.domain.member.PasswordEncoder;

/**
 * 해당 클래스에 선언한 빈이 테스트에서는 우선적으로 동작한다.
 * 차후 테스트 빈이 아닌 실제 구현체를 사용하기 위해서는 해당 빈을 삭제해야한다.
 *
 * 또한, 같은 설정 클래스를 사용하는 테스트는 컨테이너를 공유하기 때문에 속도적인 측면에서도 빠르다.
 */
@TestConfiguration
public class SplearnTestConfiguration {
	@Bean
	public EmailSender emailSender () {
		return (email, subject, body) -> System.out.println("sending Email :: " + email);
	}

	@Bean
	public PasswordEncoder passwordEncoder () {
		return MemberFixture.createPasswordEncoder();
	}
}
