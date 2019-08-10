package tajiri.example.jwt.auth.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class LoginUser {

    @Id
    private String username;

    private String password;

}
