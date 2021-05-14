package bstorm.akimt.demoJwt.security.jwt_support;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static bstorm.akimt.demoJwt.security.SecurityConstants.*;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    public String createToken( String username, List<String> roles ){
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date( System.currentTimeMillis() + EXPIRATION_TIME ))
                .withClaim("roles", roles)
                .sign( Algorithm.HMAC512(JWT_KEY) );

        return TOKEN_PREFIX + token;
    }

    // Permet de récupérer un token 'nu' à partir d'une requête s'il existe
    public String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if( bearer != null && bearer.startsWith( TOKEN_PREFIX ) )
            return bearer.substring( TOKEN_PREFIX.length() );

        return null;
    }

    // Valide un token sur base de 2 critères: la présence du username et la validité de l'Algorithm d'encryptage
    public boolean validateToken(String token) {

        try {
            String username = JWT.require(Algorithm.HMAC512( JWT_KEY ))
                    .build()
                    .verify( token.replace(TOKEN_PREFIX, "") )
                    .getSubject();

            return username != null;
        }
        catch ( JWTVerificationException ex ) {
            return false;
        }

    }

    // Récupération du username présent dans le token validé et 'nu'
    public String getUsername( String token ) {
        return JWT.decode(token).getSubject();
    }

    // Permet de créer un objet Authentication sur base d'un token 'nu' et validé
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername( getUsername(token) );
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"",userDetails.getAuthorities());
    }

}
