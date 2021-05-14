package bstorm.akimt.demoJwt.models.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserForm {

    private String username;
    private String password;
    private List<String> roles;

}
