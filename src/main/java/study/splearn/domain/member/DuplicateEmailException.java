package study.splearn.domain.member;

public class DuplicateEmailException extends RuntimeException{
	public DuplicateEmailException (String s) {
		super(s);
	}
}
