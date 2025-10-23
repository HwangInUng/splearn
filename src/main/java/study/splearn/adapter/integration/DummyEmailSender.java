package study.splearn.adapter.integration;

import org.springframework.stereotype.Component;
import study.splearn.application.member.required.EmailSender;
import study.splearn.domain.shared.Email;

@Component
public class DummyEmailSender implements EmailSender {

	@Override
	public void send (Email email, String subject, String body) {
		System.out.println("email sender.");
	}
}
