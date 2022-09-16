package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryTest {
    private final ItemRepository itemRepository;
    private final Pageable pageable = PageRequest.of(0, Constants.PAGE_SIZE_NUM);

    @BeforeEach
    void setUp() {
        itemRepository.save(new Item(
                null,
                "Отвертка",
                "Плоская отвертка",
                true,
                1L,
                1L,
                new ArrayList<>()));
        itemRepository.save(new Item(
                null,
                "Отвертка",
                "Крестовая отвертка",
                true,
                3L,
                1L,
                new ArrayList<>()));
        itemRepository.save(new Item(
                null,
                "Отвертка",
                "Отвертка со сменной битой",
                false,
                3L,
                1L,
                new ArrayList<>()));
        itemRepository.save(new Item(
                null,
                "Молоток",
                "Молоток для плотника",
                true,
                3L,
                1L,
                new ArrayList<>()));
    }

    @ParameterizedTest
    @CsvSource({
            "Отвертка, 2",
            "отвертка, 2",
            "Молоток, 1",
            "'Отвертка со сменной битой', 0",
            "Клей, 0"
    })
    void findItemByAvailableAndQueryContainWithIgnoreCase(String queryText, int expectedSize) {
        var itemList = itemRepository.findItemByAvailableAndQueryContainWithIgnoreCase(pageable, queryText);
        assertThat(expectedSize).isEqualTo(itemList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 0",
            "3, 3"
    })
    void findAllByOwnerId(Long userId, int expectedSize) {
        var itemList = itemRepository.findAllByOwnerId(pageable, userId);
        assertThat(expectedSize).isEqualTo(itemList.size());
    }
}