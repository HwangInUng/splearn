package study.splearn.domain;

public record MemberCreateReqeust(String email, String nickname, String passwordHash) {
}
