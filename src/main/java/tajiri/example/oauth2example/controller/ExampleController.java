package tajiri.example.oauth2example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping(value = "/example")
    public String getExampleMessage() {
        return "oauth2 example!";
    }
}
