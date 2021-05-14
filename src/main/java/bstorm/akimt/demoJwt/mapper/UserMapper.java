package bstorm.akimt.demoJwt.mapper;

import bstorm.akimt.demoJwt.entity.User;
import bstorm.akimt.demoJwt.models.dto.UserDTO;
import bstorm.akimt.demoJwt.models.form.UserForm;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User formToEntity(UserForm form){
        if( form == null )
            return null;

        return User.builder()
                .username(form.getUsername())
                .password(form.getPassword())
                .roles(form.getRoles())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public UserDTO toDTO(User user) {
        if(user == null)
            return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }

}
