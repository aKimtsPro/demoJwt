package bstorm.akimt.demoJwt.service;

import bstorm.akimt.demoJwt.entity.User;
import bstorm.akimt.demoJwt.exception.CustomException;
import bstorm.akimt.demoJwt.mapper.UserMapper;
import bstorm.akimt.demoJwt.models.dto.UserDTO;
import bstorm.akimt.demoJwt.models.form.UserForm;
import bstorm.akimt.demoJwt.repository.UserRepository;
import bstorm.akimt.demoJwt.security.jwt_support.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authManager;

    public UserServiceImpl(UserRepository repo, UserMapper mapper, PasswordEncoder encoder, JwtTokenProvider tokenProvider, AuthenticationManager authManager) {
        this.repo = repo;
        this.mapper = mapper;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
        this.authManager = authManager;
    }

    @Override
    public UserDTO insert(UserForm form) {
        if( repo.existsByUsername(form.getUsername()) )
            throw new CustomException("username déjà pris", HttpStatus.UNPROCESSABLE_ENTITY);

        User toInsert = mapper.formToEntity(form);
        toInsert.setPassword( encoder.encode(toInsert.getPassword()) );
        return mapper.toDTO( repo.save(toInsert) );
    }

    @Override
    public UserDTO signIn(UserForm form) {

        try{
            authManager.authenticate( new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()) );
            UserDTO user = mapper.toDTO(repo.findByUsername(form.getUsername()).orElseThrow());
            user.setToken( tokenProvider.createToken(user.getUsername(), user.getRoles()) );
            return user;
        }
        catch (AuthenticationException ex){
            throw new CustomException("Username/Password invalide(s)",HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @Override
    public UserDTO signUp(UserForm form) {
        form.setRoles( Collections.singletonList("ROLE_USER") );
        UserDTO dto = insert( form );
        dto.setToken( tokenProvider.createToken(dto.getUsername(), dto.getRoles()) );
        return dto;
    }
}
