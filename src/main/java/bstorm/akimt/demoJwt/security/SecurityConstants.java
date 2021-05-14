package bstorm.akimt.demoJwt.security;

public class SecurityConstants {

    public static final String JWT_KEY = "M@_Cl3F_D'3nCryP7aG3";
    public static final long EXPIRATION_TIME = 864000000; // 10 jours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_KEY = "Authorization";
    public static final String REGISTER_URL = "/users/sign_up";
    public static final String LOGIN_URL = "/users/sign_in";

}
