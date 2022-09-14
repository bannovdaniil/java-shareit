package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.Request;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestRepositoryTest {
    private final RequestRepository requestRepository;
    private Request request1;
    private Request request2;
    private Request request3;
    private final Pageable pageable = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        request1 = requestRepository.save(new Request(
                null,
                "description 1",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()));
        request2 = requestRepository.save(new Request(
                null,
                "description 1",
                3L,
                LocalDateTime.now(),
                new ArrayList<>()));
        request3 = requestRepository.save(new Request(
                null,
                "description 1",
                3L,
                LocalDateTime.now(),
                new ArrayList<>()));
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
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