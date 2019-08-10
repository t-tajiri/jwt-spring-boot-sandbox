package tajiri.example.jwt.auth.service.impl;

import org.springframework.stereotype.*;
import tajiri.example.jwt.auth.model.*;
import tajiri.example.jwt.auth.repository.*;
import tajiri.example.jwt.auth.service.*;

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
