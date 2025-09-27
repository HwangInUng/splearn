package study.splearn.domain;

public class DuplicateEmailException extends RuntimeException{
	public DuplicateEmailException (String s) {
		super(s);
	}
}
