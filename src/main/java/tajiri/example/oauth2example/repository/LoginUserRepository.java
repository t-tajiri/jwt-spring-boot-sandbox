package tajiri.example.oauth2example.repository;

import org.springframework.data.repository.*;
import tajiri.example.oauth2example.model.*;

public interface LoginUserRepository extends CrudRepository<LoginUser, String> {
}
