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


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerInterationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;


    @DisplayName(value = "when call GET on /users " +
            "then return empty array when there's no user in database")
    @Test
    void test() throws Exception {

        //when
        mockMvc.perform(get("/users"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().string("[]"));
    }


    @DisplayName(value =
            "given 1 user in database, " +
            "when call GET on /users, " +
            "then array containing this user is returned")
    @Test
    void test1() throws Exception {
        // given
        User user = new User("Jan","Kowalski");
        userRepository.save(user);
        //when
        mockMvc.perform(get("/users"))
        //then
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].firstname",is("Jan")))
        .andExpect(jsonPath("$[0].lastname",is("Kowalski")));
    }

    @DisplayName(value = "given 2 users to database," +
            " when call GET on /users/{id}," +
            " then return user with given id")
    @Test
    void test2() throws Exception {
        //given
        User user = new User("Jan","Kowalski");
        User user1 = new User("Krzysztof","Nowak");
        userRepository.save(user);
        userRepository.save(user1);
        //when
        mockMvc.perform(get("/users/{id}",user1.getId()))
        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstname",is("Krzysztof")))
        .andExpect(jsonPath("$.lastname",is("Nowak")));
    }

    @DisplayName(value = "when post user data on /users" +
            " then user is created in database")
    @Test
    void test3() throws Exception {

        //given
        String user= "{\"firstname\":\"Jan \", \"lastname\":\"Kowalski\"}";

        //when
        mockMvc.perform(post("/users")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());
        Iterable<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(1);
    }

    @DisplayName(value = "given 2 users in database " +
            "when call delete on /users/{id}" +
            "then chosen user is deleted")
    @Test
    void test4() throws Exception {

        //given
        User user = new User("Jan","Kowalski");
        User user1 = new User("Krzysztof","Nowak");
        userRepository.save(user);
        userRepository.save(user1);

        //when
        mockMvc.perform(delete("/users/{id}",user1.getId()))

        //then
        .andExpect(status().isOk());
        Iterable<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(1);
        assertThat(allUsers).extracting(User::getFirstname).containsOnly("Jan");
    }
}
