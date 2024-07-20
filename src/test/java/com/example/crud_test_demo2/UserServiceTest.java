package com.example.crud_test_demo2;

import com.example.crud_test_demo2.entities.User;
import com.example.crud_test_demo2.repositories.UserRepository;
import com.example.crud_test_demo2.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkUserActivation() throws Exception {
        User user = new User();
        user.setWorking(true);
        user.setName("Erika");
        user.setSurname("Longo");

        User userFromDB = userRepository.save(user);
        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getId()).isNotNull();

        User userFromService = userService.setUserActivationStatus(user.getId(), true);
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();
        assertThat(userFromService.getWorking()).isTrue();

        User userFromFind = userRepository.findById(user.getId()).get();
        assertThat(userFromFind).isNotNull();
        assertThat(userFromFind.getId()).isNotNull();
        assertThat(userFromFind.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFromFind.getWorking()).isTrue();
    }
}
