# project Share It

Сервис для шеринга (от англ. share — «делиться») вещей.
Шеринг как экономика совместного использования набирает сейчас всё большую полярность.

Проект обмена вещами, возможно далеко похожее на Avito.

# History:
### 2022-09-02
## Добавление отзывов
- пользователи могут оставлять отзывы на вещь после того, как взяли её в аренду.
- JPARepository для Comment

### 2022-09-01
## Добавление дат бронирования при просмотре вещей
- Теперь нужно, чтобы владелец видел даты последнего и ближайшего следующего бронирования для каждой вещи, когда просматривает список (GET /items).

## Реализация функции бронирования
- JpaRepository для Booking

### 2022-08-29
- JpaRepository для Users
- JpaRepository для Items

### 2022-08-28
## Настройка JPA
- проставил аннотации JPA для моделей

### 2022-08-27
## Создание базы данных

- Напишисал SQL-код для создания всех таблиц и сохраните его в файле resources/schema.sql

## Init
В этом спринте разработка будет вестись в ветке add-bookings
- добавил зависимость spring-boot-starter-data-jpa и драйвер postgresql в файл pom.xml.

#### 2022-08-15
### Разработка контроллеров

- ItemController
#### 2022-08-14
- UserController

### Создание DTO-объектов и мапперов

- User модель
- Item модель

### Реализация модели данных

Создание структуры данных

- User
- Item
- Booking
- Requests

### Создайте отдельную ветку

У вас уже готов шаблон проекта с использованием Spring Boot. Создайте ветку **add-controllers** и
переключитесь на неё — в этой ветке будет вестись вся разработка для первого спринта.

### Sprint 1

# java-shareit

Template repository for Shareit project.
