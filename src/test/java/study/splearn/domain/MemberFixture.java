package study.splearn.domain;

public class MemberFixture {
	public static MemberRegisterRequest createMemberRegisterReqeust (String email) {
		return new MemberRegisterRequest(email, "nickname", "secret");
	}

	public static MemberRegisterRequest createMemberRegisterRequest () {
		return createMemberRegisterReqeust("test@test.com");
	}

	public static PasswordEncoder createPasswordEncoder () {
		return new PasswordEncoder() {
			@Override
			public String encode (String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches (String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};
	}
}
