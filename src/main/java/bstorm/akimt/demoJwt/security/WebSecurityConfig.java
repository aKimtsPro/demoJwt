package bstorm.akimt.demoJwt.security;

import bstorm.akimt.demoJwt.security.jwt_support.JwtAuthenticationFilter;
import bstorm.akimt.demoJwt.security.jwt_support.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Permet d'utiliser les methodes et variables static de la classe sans la nommer
import static bstorm.akimt.demoJwt.security.SecurityConstants.*;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    -- AuthenticationManagerBuilder a déjà ce comportement par défaut
//    private final UserDetailsService userDetailsService;
//
//    public WebSecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService( userDetailsService );
//    }

    private final JwtTokenProvider tokenProvider;

    public WebSecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Désactive la gestion des attaques CSRF (FORGERY TOKEN)
        http.csrf().disable();

        // Point d'entré
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, REGISTER_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();

        // Configuration des filtres
        http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // H2-console
        http.headers()
                .frameOptions()
                .disable();

        // Http BASIC
        http.httpBasic();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
