package pl.sda.restdemo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        //given
        User user = new User("Kowalski","Jan");
        //when
        mockMvc.perform(get("/users"))
        //then
        .andExpect(co)
    }
}
