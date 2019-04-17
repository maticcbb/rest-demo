package pl.sda.restdemo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//adnotacja oznacza że jest to test Springa
@SpringBootTest
// adnotacja konfiguruje nasz obiekt MockMvc dzieki ktoremu możemy wykonywać zapytania
@AutoConfigureMockMvc
// czyści naszą baze za każdym wykonanym testem i uruchamia od nowa (izoluje testy)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerInterationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;


    // test dla naszej metody w UserControllerze getAll()
    @DisplayName(value = "when call GET on /users " +
            "then return empty array when there's no user in database")
    @Test
    void test() throws Exception {

        //when
        // test wykonuje zapytanie GET  do pustej bazy pod adresem /users

        mockMvc.perform(get("/users"))

        //then
        // i czy status zapytania jest 200 czyli poprawny
        .andExpect(status().isOk())
        // i sprawdza czy dostaniemy w odpowiedzi pusta tablice
        .andExpect(content().string("[]"));
    }


    @DisplayName(value =
            "given 1 user in database, " +
            "when call GET on /users, " +
            "then array containing this user is returned")
    @Test
    void test1() throws Exception {
        // given
        // tworzymy obiekt usera
        User user = new User("Jan","Kowalski");
        // zapisujemy go do repozytorium (taka chwilowa baza)
        userRepository.save(user);
        //when
        // wykonujemy zapytanie get pod adresem /users czyli wywołujemy metode getAll()- patrz UserController
        mockMvc.perform(get("/users"))
        //then
        // oczekujemy że tablica którą otrzymamy będzie miała wielkość 1 (bo wyżej dodalismy tylko jeden obiekt)
        .andExpect(jsonPath("$", hasSize(1)))
        // oczekujemy też że pierwszy obiekt zwroconej tablicy $[0] o polu .firstname jest równy 'Jan'
        .andExpect(jsonPath("$[0].firstname",is("Jan")))
        // oczekujemy też że pierwszy obiekt zwroconej tablicy $[0] o polu .lastname jest równy 'Kowalski'
        .andExpect(jsonPath("$[0].lastname",is("Kowalski")));
    }

    @DisplayName(value = "given 2 users to database," +
            " when call GET on /users/{id}," +
            " then return user with given id")
    @Test
    void test2() throws Exception {
        //given
        // tworzymy 2 obiekty do dodania
        User user = new User("Jan","Kowalski");
        User user1 = new User("Krzysztof","Nowak");
        // zapisujemy gotowa metoda z repozytorium save obydwa obiekty (mamy teraz w bazie 2 obiekty)
        userRepository.save(user);
        userRepository.save(user1);
        //when
        // wywołujemy zapytanie GET z argumentem user1.getId czyli , wyciagamyt Krzysztofa Nowaka
        mockMvc.perform(get("/users/{id}",user1.getId()))
        //then
        // czy serwer zwraca status 200
        .andExpect(status().isOk())
        // oczekujemy też że pierwszy obiekt zwroconej tablicy $[0] o polu .firstname jest równy 'Krzysztof'
        .andExpect(jsonPath("$.firstname",is("Krzysztof")))
        // oczekujemy też że pierwszy obiekt zwroconej tablicy $[0] o polu .lastname jest równy 'Nowak'
        .andExpect(jsonPath("$.lastname",is("Nowak")));
    }

    @DisplayName(value = "when post user data on /users" +
            " then user is created in database")
    @Test
    void test3() throws Exception {
        //given
        //string ktory wysyłamy jako JSON , czyli jeden obiekt z polami firstname i lastname ,
        // id jest przydzielone automatycznie przy dodaniu wiec nie podajemy (@GeneratedValue)
        String user= "{\"firstname\":\"Jan \", \"lastname\":\"Kowalski\"}";

        //when
        //wykonuje zapytanie POST  pod adres /users  czyli metoda  addOne() w UserController
        mockMvc.perform(post("/users")
                // wysyłamy nasz JSON
                .content(user)
                // podajemy typ jaki wysyłamy
                .contentType(MediaType.APPLICATION_JSON))

        //then
                // oczekujemy że serwer zwroci status 200
                .andExpect(status().isOk());
        // wykonujemy nasza metode findAll() ktora zwraca wszystkie obiekty (w tym przypadku jeden)
        Iterable<User> allUsers = userRepository.findAll();
        // i sprawdza czy ta tablica ma rozmiar 1
        assertThat(allUsers).hasSize(1);
    }

    @DisplayName(value = "given 2 users in database " +
            "when call delete on /users/{id}" +
            "then chosen user is deleted")
    @Test
    void test4() throws Exception {

        //given
        // tworzymy 2 obiekty do dodania
        User user = new User("Jan","Kowalski");
        User user1 = new User("Krzysztof","Nowak");
        // zapisujemy gotowa metoda z repozytorium save obydwa obiekty (mamy teraz w bazie 2 obiekty)
        userRepository.save(user);
        userRepository.save(user1);

        //when
        // wykonujemy zapytanie DELETE na dany adres i usuwamy user1 czyli Krzysztofa Nowaka
        mockMvc.perform(delete("/users/{id}",user1.getId()))

        //then
         // sprawdzamy czy serwer zwraca status 200
        .andExpect(status().isOk());
        // wykonujemy metode findAll() aby pobrac wszystkie obiekty z naszej bazy
        Iterable<User> allUsers = userRepository.findAll();
        // sprawdzamy czy  nasza baza zawiera 1 obiekt (ponieważ usuneliśmy wcześniej jeden )
        assertThat(allUsers).hasSize(1);
        // i czy firstname obiektu ktory został to "Jan"
        assertThat(allUsers).extracting(User::getFirstname).containsOnly("Jan");
    }
}
