package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i " +
            " FROM Item as i " +
            " WHERE " +
            " i.available = true" +
            " AND (" +
            " LOWER(i.name) LIKE LOWER(CONCAT('%',:queryText,'%')) " +
            " OR  LOWER(i.description) LIKE LOWER(CONCAT('%',:queryText,'%'))" +
            " )"
    )
    List<Item> findItemByAvailableAndQueryContainWithIgnoreCase(String queryText);

    List<Item> findAllByOwner(Long userId);
}
