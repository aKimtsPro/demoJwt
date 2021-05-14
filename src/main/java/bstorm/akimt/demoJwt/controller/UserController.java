package bstorm.akimt.demoJwt.controller;

import bstorm.akimt.demoJwt.models.dto.UserDTO;
import bstorm.akimt.demoJwt.models.form.UserForm;
import bstorm.akimt.demoJwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserForm form) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.signUp(form));
    }

    @PostMapping("/sign_in")
    public ResponseEntity<UserDTO> signIn(@RequestBody UserForm form) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.signIn(form));
    }

    @PostMapping("/insert")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> insert(@RequestBody UserForm form) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(service.insert(form));
    }

//    @PostMapping("/insert2")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @ResponseStatus( HttpStatus.ACCEPTED )
//    public UserDTO insert2(@RequestBody UserForm form) {
//        return service.insert(form);
//    }
}
