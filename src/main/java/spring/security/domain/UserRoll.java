package spring.security.domain;

import lombok.Getter;

@Getter
public enum UserRoll {

    //열거형 상수
    //Spring Security에서 권한 문자열은 "ROLE_" 접두어를 붙이는 것이 규칙
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRoll(String value) {
        this.value = value;
    }
    //열거형 상수(ADMIN, USER)에 연결된 문자열 값을 저장하기 위해 변수 선언
    private String value;
}