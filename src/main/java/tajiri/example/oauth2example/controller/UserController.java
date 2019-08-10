package tajiri.example.oauth2example.controller;

import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;
import tajiri.example.oauth2example.model.*;
import tajiri.example.oauth2example.service.*;

@RestController
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService service;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService service) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.service = service;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> signUp(@RequestBody LoginUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        service.save(user);
        // @formatter:off
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                    .path("/{username}")
                                                    .buildAndExpand(user.getUsername())
                                                    .toUri();
        // @formatter:off
        return ResponseEntity.created(location).build();
    }

}
