package pl.sda.restdemo;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> getAll(){
        return userRepository.findAll() ;
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable Long id){
        return userRepository.findById(id);
    }

    @PostMapping
    public User addOne(User user){
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void removeOne(@PathVariable Long id){
        userRepository.deleteById(id);
    }


}
