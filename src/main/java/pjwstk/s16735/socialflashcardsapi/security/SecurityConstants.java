package pjwstk.s16735.socialflashcardsapi.security;

public class SecurityConstants {
    public static final String SECRET = "ABCDABCDABCD";
    public static final long EXPIRATION_TIME = 31_536_000_000L; // 365 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}
