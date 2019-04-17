package pl.sda.restdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UserController {

    @GetMapping("/users")
    public Iterable<User> getAll(){
        return new ArrayList<>();
    }

}
