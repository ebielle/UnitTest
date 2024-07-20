package com.example.crud_test_demo2;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.crud_test_demo2.controllers.UserController;
import com.example.crud_test_demo2.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void UserControllerLoads() {
        assertThat(userController).isNotNull();
    }

    private User getUserFromId(Integer id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/user/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String userJSON = result.getResponse().getContentAsString();
            User user = objectMapper.readValue(userJSON, User.class);

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNotNull();

            return user;

        } catch (Exception e) {
            return null;
        }
    }

    private User createUser() throws Exception {
        User user = new User();
        user.setWorking(true);
        user.setName("Erika");
        user.setSurname("Longo");

        return createUser(user);
    }

    private User createUser(User user) throws Exception {

        MvcResult result = createUserRequest();
        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isNotNull();

        return userFromResponse;
    }

    private MvcResult createUserRequest() throws Exception {
        User user = new User();
        user.setWorking(true);
        user.setName("Erika");
        user.setSurname("Longo");

        return createUserRequest(user);
    }

    private MvcResult createUserRequest(User user) throws Exception {
        if(user == null) return null;

        String userJSON = objectMapper.writeValueAsString(user);

        return this.mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createUserTest() throws Exception {
        User userFromResponse = createUser();
    }

    @Test
    void readUsersList() throws Exception {
        createUserRequest();

        MvcResult result = this.mockMvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> usersFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("Users in database: " + usersFromResponse.size());
        assertThat(usersFromResponse.size()).isNotZero();
    }

    @Test
    void readSingleUser() throws Exception {
        User user = createUser();
        User userFromResponse = getUserFromId(user.getId());
        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
    }

    @Test
    void updateUser() throws Exception {
        User user = createUser();

        String newName = "Andrea";
        user.setName(newName);
        String userJSON = objectMapper.writeValueAsString(user);

        MvcResult result = this.mockMvc.perform(put("/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        //Check user from PUT
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getName()).isEqualTo(newName);

        //I get the user with GET
        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getName()).isEqualTo(newName);
    }

    @Test
    void deleteUser() throws Exception {
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        this.mockMvc.perform(delete("/user/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet).isNull();
    }

    @Test
    void activateUser() throws Exception {
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/user/" + user.getId() + "/activation?working=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getWorking()).isEqualTo(true);

        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet).isNotNull();
        assertThat(userFromResponseGet.getId()).isEqualTo(user.getId());
        assertThat(userFromResponseGet.getWorking()).isEqualTo(true);
    }
}
