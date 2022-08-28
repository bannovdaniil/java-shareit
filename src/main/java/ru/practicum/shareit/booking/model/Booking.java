package ru.practicum.shareit.booking.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDate start;
    @Column(name = "end_date")
    private LocalDate end;
    @Column(name = "item_id")
    private Long item;
    @Column(name = "booker_id")
    private Long booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
