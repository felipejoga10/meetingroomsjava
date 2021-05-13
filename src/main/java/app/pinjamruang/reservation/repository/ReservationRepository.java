package app.pinjamruang.reservation.repository;

import app.pinjamruang.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStartDateBetweenAndEndDateBetween(
            LocalDateTime startDateStart,
            LocalDateTime startDateEnd,
            LocalDateTime endDateStart,
            LocalDateTime endDateEnd
    );
}
