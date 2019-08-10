package tajiri.example.jwt.auth.repository;

import org.springframework.data.repository.*;
import tajiri.example.jwt.auth.model.*;

public interface LoginUserRepository extends CrudRepository<LoginUser, String> {
}
