## 도메인 모델

### 회원(Member)
_Entity_

#### 속성
- email: 이메일 - ID
- nickname: 별명
- passwordHash: 비밀번호 해시 값
- status: 회원 상태

#### 행위
- constructor(): 회원 생성 email, nickname, passwordHash, status
- activate(): 회원 가입
- deactivate(): 회원 탈퇴

#### 규칙
- 회원 생성 후 상태는 가입 대기 상태이다.
- 일정 조건을 만족하면 가입이 완료된다.
- 가입 완료 상태에서만 탈퇴가 가능하다.

### 회원 상태(MemberStatus)
_Enum_

#### 상수
- PENDING: 가입 대기
- ACTIVE: 가입 완료
- DEACTIVATED: 탈퇴