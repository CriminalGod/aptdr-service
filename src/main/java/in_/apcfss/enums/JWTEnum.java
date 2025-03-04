package in_.apcfss.enums;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum JWTEnum implements Serializable {

    ACCESS_TOKEN("access-token"),
    REFRESH_TOKEN("refresh-token"),
    AUTHORITIES_CLAIM("auth"),
    AUTHORIZATION_HEADER("Authorization"),
    REFRESH_TOKEN_HEADER("Refresh-Token"),
    ACCESS_TOKEN_ROLE("ACCESS_TOKEN_ROLE"),
    REFRESH_TOKEN_ROLE("REFRESH_TOKEN_ROLE"),
    SESSION_TIMEOUT("session_timeout"),
    BEARER_TOKEN_PREFIX("Bearer "),
    USER_ID("userId"),
    USER_NAME("userName"),
    USER_EMAIL("userEmail"),
    USER_TYPE("userType"),
    USER_TIME_ZONE("userTimeZone"),
    TIME_DIVISION("Time-Division");

    private final String value;

    JWTEnum(String value) {
        this.value = value;
    }

}
