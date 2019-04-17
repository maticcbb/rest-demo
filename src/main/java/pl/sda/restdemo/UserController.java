package pl.sda.restdemo;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;


// Adnotacja oznaczająca klase jako kontroler
@RestController
// Adnotacja podaje pod jakim linkiem  znajduje się cała klasa,
// czyli wszystkie metody kontollera zaczynają się od /users
@RequestMapping("/users")
public class UserController {

    // Obiekt stworzonej klasy naszegpo repozytorium,
    // udostępnia nam on metody do zapisywania,
    // znajdywania, usuwania obiektów i wiele innych gotowych metod
    private UserRepository userRepository;

    // Konstruktor  w którym tworzymy userRepository
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Adnotacja daje znać że będzie wykonywała zgodnie z protokołem HTTP żądanie GET (czyli będzie ODBIERAĆ jakieś dane)
    @GetMapping
    // Metoda zwraca wszystkie obiekty dlatego jest w Iterable
    public Iterable<User> getAll(){
        // nasz wcześniej zainicjalizowany userRepository posiada gotowe metody takie jak FindAll(),
        // ktory zwraca wszystkie obiekty
        return userRepository.findAll() ;
    }

    // Adnotacja też wykonuje żądanie GET, ale do naszego adresu /users dodajemy /{id}, czyli będzie dostępny pod /users/{id}
    // id jest w klamrach ponieważ jest to zmienna którą  będziemy pobierać z argumentu metody.
    @GetMapping("/{id}")
    // metoda zwraca pojedyńczy obiekt User  o numerze id jaki podaliśmy w argumencie
    // adnotacja @PathVariable oznacza że /{id} w ścieżce to id podane w argumencie metody
    // userRepository ma też gotową metode findById() ktora zwraca obiekt o danym id
    public Optional<User> getById(@PathVariable Long id){
        return userRepository.findById(id);
    }

    // Adnotacja która zgonie z protokołem HTTP wykonuje żądanie POST, (czyli WYSYŁA dane )
    @PostMapping
    // Metoda przyjmuje w argumencie jeden obiekt User , który chcielibyśmy wysłać
    public User addOne(User user){
        // kolejna gotowa metoda z naszego userRepository ,
        // save() która zapisuje to co podamy w argumencie,
        // my podaliśmy obiekt user, który ma się zapisać
        return userRepository.save(user);
    }

    // Adnotacja która zgonie z protokołem HTTP wykonuje żądanie DELETE, (czyli USUWA dane )
    // wpisana ścieżka  /{id}  oznacza że  operacja będzie dostępna pod ścieżką /users/{id}
    @DeleteMapping("/{id}")
    // też podajemy adnotacje @PathVariable, bo dajemy w argumencie id  obiektu który chcemy usunąć
    // userRepository ma kolejna gotową metodę  deleteById() czyli usuwa obiekt o danym id
    public void removeOne(@PathVariable Long id){
        userRepository.deleteById(id);
    }


}
