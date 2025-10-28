package study.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.splearn.domain.member.DuplicateEmailException;
import study.splearn.domain.member.DuplicateProfileException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
	/*
	 * ProblemDetail
	 *
	 * Spring 6.0 부터 사용 가능
	 * RFC9457 적용
	 *
	 * 서버에서 발생한 예외를 클라이언트에서 쉽게 이해할 수 있도록 조치하기 위해 적용
	 * 내부적으로 정의된 JSON 포맷으로 응답을 주면된다.
	 *
	 * 별도의 포맷을 만들 필요없이 해당 클래스를 표준으로 사용할 수 있다.
	 * application/problem+json
	 * */
	@ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
	public ProblemDetail emailExceptionHandler (DuplicateEmailException exception) {
		return getProblemDetail(HttpStatus.CONFLICT, exception);
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleException (Exception exception) {
		return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception);
	}

	private static ProblemDetail getProblemDetail (HttpStatus status, Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

		problemDetail.setProperty("exception", exception.getClass().getSimpleName());
		problemDetail.setProperty("timestamp", LocalDateTime.now());

		return problemDetail;
	}
}
