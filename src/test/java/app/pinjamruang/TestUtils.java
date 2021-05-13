package app.pinjamruang;

import app.pinjamruang.reservation.dto.CreateReservationDto;
import app.pinjamruang.reservation.model.Reservation;
import app.pinjamruang.room.dto.CreateRoomDto;
import app.pinjamruang.room.model.Room;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {
    public static final DateTimeFormatter DTO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Room createDummyRoom() {
        return new Room(
                "Room 1",
                10,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00")
        );
    }

    public static Reservation createDummyReservation(Room room) {
        return new Reservation(
                LocalDateTime.parse("2019-06-12 10:00", DTO_DATETIME_FORMATTER),
                LocalDateTime.parse("2019-06-12 12:00", DTO_DATETIME_FORMATTER),
                10,
                "Testing",
                room
        );
    }

    public static CreateReservationDto createDummyReservationDto() {
        return new CreateReservationDto(
                "2019-06-12 10:00",
                "2019-06-12 12:00",
                10,
                "Testing",
                1L
        );
    }

    public static CreateRoomDto createDummyRoomDto() {
        return new CreateRoomDto(
                "Room 1",
                10,
                "09:00",
                "18:00"
        );
    }
}
