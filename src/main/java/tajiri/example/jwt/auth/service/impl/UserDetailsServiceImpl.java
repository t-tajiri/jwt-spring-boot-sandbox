package tajiri.example.jwt.auth.service.impl;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import tajiri.example.jwt.auth.repository.*;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private LoginUserRepository loginUserRepository;

    public UserDetailsServiceImpl(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = loginUserRepository.findById(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("user is not found. username: " + username);
        }

        return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
    }

}
