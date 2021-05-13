package app.pinjamruang.reservation.repository;

import app.pinjamruang.reservation.model.Reservation;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static app.pinjamruang.TestUtils.createDummyReservation;
import static app.pinjamruang.TestUtils.createDummyRoom;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTests {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoomRepository roomRepository;

    @Before
    public void setUp() {
        roomRepository.save(createDummyRoom());
    }

    @Test
    public void jpaRepositoryBasicTest() {
        Room room = roomRepository.getOne(1L);

        Reservation reservation = reservationRepository.save(createDummyReservation(room));

        assertEquals(reservationRepository.getOne(reservation.getId()), reservation);
    }

    @Test
    public void findByStartDateBetweenAndEndDateBetween_foundAnOverlappingReservation_success() {
        Room room = roomRepository.getOne(1L);

        Reservation existingReservation = reservationRepository.save(createDummyReservation(room));

        LocalDateTime startDate = existingReservation.getStartDate();
        LocalDateTime endDate = existingReservation.getEndDate();

        List<Reservation> result = reservationRepository.findByStartDateBetweenAndEndDateBetween(startDate, endDate, startDate, endDate);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), existingReservation);
    }

    @Test
    public void findByStartDateBetweenAndEndDateBetween_noOverlappingReservation_success() {
        Room room = roomRepository.getOne(1L);

        Reservation existingReservation = reservationRepository.save(createDummyReservation(room));

        LocalDateTime startDate = existingReservation.getStartDate().minusMinutes(2L);
        LocalDateTime endDate = existingReservation.getStartDate().minusMinutes(1L);

        List<Reservation> result = reservationRepository.findByStartDateBetweenAndEndDateBetween(startDate, endDate, startDate, endDate);

        assertEquals(result.size(), 0);
    }
}
