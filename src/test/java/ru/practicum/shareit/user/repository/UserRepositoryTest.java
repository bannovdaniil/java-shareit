package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(new User(null, "User1", "email1@mail.com"));
        userRepository.save(new User(null, "User2", "email2@gmail.com"));
        userRepository.save(new User(null, "User3", "email3@hotmail.com"));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({"'email1@mail.com', true",
            "'email2@gmail.com', true",
            "'email3@hotmail.com', true",
            "'notfound@mail.com', false"
    })
    void findUserByEmail(String email, boolean isPresentExpected) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        assertEquals(isPresentExpected, userByEmail.isPresent());
        if (isPresentExpected) {
            assertEquals(email, userByEmail.get().getEmail());
        } else {
            assertThat(userByEmail).isEmpty();
        }
    }

}