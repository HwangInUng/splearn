package study.splearn.domain.member;

public class DuplicateProfileException extends RuntimeException {
	public DuplicateProfileException (String s) {
		super(s);
	}
}
