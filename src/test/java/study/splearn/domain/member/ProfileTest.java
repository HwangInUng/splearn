package study.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {
	@Test
	void profile () {
	    new Profile("iwhwang");
	    new Profile("iwhwang1234");
	    new Profile("923482");
	    new Profile("");
	}

	@Test
	void profileFail () {
		assertThatThrownBy(() -> new Profile("t".repeat(16))).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void url () {
	    var profile = new Profile("iwhwang");
		
		assertThat(profile.url()).isEqualTo("@iwhwang");
	}
}