package tajiri.example.oauth2example.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class LoginUser {

    @Id
    private String username;

    private String password;

}
