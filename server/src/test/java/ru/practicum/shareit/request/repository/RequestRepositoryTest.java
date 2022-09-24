package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:hsqldb:mem:${random.uuid}"})
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestRepositoryTest {
    private final RequestRepository requestRepository;
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);

    @BeforeEach
    void setUp() {
        requestRepository.save(new Request(
                null,
                "description 1",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()));
        requestRepository.save(new Request(
                null,
                "description 1",
                3L,
                LocalDateTime.now(),
                new ArrayList<>()));
        requestRepository.save(new Request(
                null,
                "description 1",
                3L,
                LocalDateTime.now(),
                new ArrayList<>()));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 0",
            "3, 2"
    })
    void findByRequestorId(Long userId, int expectedSize) {
        List<Request> requestList = requestRepository.findByRequestorId(pageable, userId);
        assertThat(expectedSize).isEqualTo(requestList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2",
            "2, 3",
            "3, 1"
    })
    void findByRequestorIdIsNot(Long userId, int expectedSize) {
        List<Request> requestList = requestRepository.findByRequestorIdIsNot(pageable, userId).toList();
        assertThat(expectedSize).isEqualTo(requestList.size());
    }
}