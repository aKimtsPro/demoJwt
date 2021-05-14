package bstorm.akimt.demoJwt.service;

import bstorm.akimt.demoJwt.models.dto.UserDTO;
import bstorm.akimt.demoJwt.models.form.UserForm;

public interface UserService {

    UserDTO insert(UserForm form);
    UserDTO signIn(UserForm form);
    UserDTO signUp(UserForm form);

}
