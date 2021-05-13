package app.pinjamruang.reservation.controller;

import app.pinjamruang.reservation.dto.CreateReservationDto;
import app.pinjamruang.reservation.model.Reservation;
import app.pinjamruang.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService service;

    @GetMapping("/")
    public List<Reservation> getAllReservations() {
        return this.service.getAllReservations();
    }

    @GetMapping("/{reservationId}")
    public Reservation getReservationById(@PathVariable Long reservationId) {
        return this.service.getReservationById(reservationId);
    }

    @PostMapping("/")
    public Reservation createReservation(@Valid @RequestBody CreateReservationDto reservationDto) {
        return this.service.createReservation(reservationDto);
    }

    @PutMapping("/{reservationId}")
    public Reservation updateReservation(@PathVariable Long reservationId, @Valid @RequestBody CreateReservationDto reservationDto) {
        return this.service.updateReservation(reservationId, reservationDto);
    }

    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {
        this.service.deleteReservation(reservationId);
    }
}
