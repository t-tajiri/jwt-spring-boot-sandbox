package tajiri.example.oauth2example.service.impl;

import org.springframework.stereotype.*;
import tajiri.example.oauth2example.model.*;
import tajiri.example.oauth2example.repository.*;
import tajiri.example.oauth2example.service.*;

@Service
public class UserServiceImpl implements UserService {

    private LoginUserRepository repository;

    public UserServiceImpl(LoginUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(LoginUser user) {
        repository.save(user);
    }

}
