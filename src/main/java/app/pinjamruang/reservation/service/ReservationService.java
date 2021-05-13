package app.pinjamruang.reservation.service;

import app.pinjamruang.reservation.dto.CreateReservationDto;
import app.pinjamruang.reservation.exception.RoomNotAvailableException;
import app.pinjamruang.reservation.model.Reservation;
import app.pinjamruang.reservation.repository.ReservationRepository;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;

    public List<Reservation> getAllReservations() {
        return this.reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) throws ResourceNotFoundException {
        Reservation result = this.reservationRepository.getOne(id);

        if (result == null) {
            throw new ResourceNotFoundException(String.format("Can\'t found any reservation with id %d", id));
        }

        return result;
    }

    public Reservation createReservation(CreateReservationDto reservationDto) throws RoomNotAvailableException {
        Reservation newReservation = convertDtoToReservation(reservationDto);

        validateReservation(newReservation);

        return this.reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long reservationId, CreateReservationDto reservationDto) throws RoomNotAvailableException, ResourceNotFoundException {
        Reservation reservation = getReservationById(reservationId);

        updateReservationFromDto(reservation, reservationDto);
        validateReservation(reservation);

        return this.reservationRepository.save(reservation);
    }

    public void deleteReservation(Long reservationId) throws ResourceNotFoundException {
        getReservationById(reservationId);

        this.reservationRepository.deleteById(reservationId);
    }

    private void validateReservation(Reservation reservation) throws RoomNotAvailableException {
        if (!roomHasEnoughCapacity(reservation)) {
            throw new RoomNotAvailableException("Given room do not have enough capacity.");
        }

        if (!roomOnOperationalTime(reservation)) {
            throw new RoomNotAvailableException("Given room is not in operational time at given start and end time.");
        }

        if (roomIsReserved(reservation)) {
            throw new RoomNotAvailableException("Given room is already reserved at given time.");
        }

        if (!reservationWithinOneDay(reservation)) {
            throw new RoomNotAvailableException("Reservations can not span for more than one day.");
        }
    }

    private boolean roomHasEnoughCapacity(Reservation reservation) {
        Room room = reservation.getRoom();

        return reservation.getAttendees() <= room.getCapacity();
    }

    private boolean roomOnOperationalTime(Reservation reservation) {
        Room room = reservation.getRoom();
        LocalTime startTime = reservation.getStartDate().toLocalTime();
        LocalTime endTime = reservation.getEndDate().toLocalTime();

        return (room.getOpenTime().isBefore(startTime) && room.getCloseTime().isAfter(endTime));
    }

    private boolean roomIsReserved(Reservation reservation) {
        LocalDateTime startDate = reservation.getStartDate();
        LocalDateTime endDate = reservation.getEndDate();
        List<Reservation> reservationsWithOverlappingDate =
                this.reservationRepository.findByStartDateBetweenAndEndDateBetween(
                        startDate, endDate, startDate, endDate);

        return reservationsWithOverlappingDate.size() > 0;
    }

    private boolean reservationWithinOneDay(Reservation reservation) {
        LocalDateTime startDate = reservation.getStartDate();
        LocalDateTime endDate = reservation.getEndDate();

        return startDate.getDayOfYear() == endDate.getDayOfYear();
    }

    private Reservation convertDtoToReservation(CreateReservationDto dto) {
        Reservation reservation = new Reservation();
        updateReservationFromDto(reservation, dto);

        return reservation;
    }

    private void updateReservationFromDto(Reservation reservation, CreateReservationDto dto) {
        reservation.setAgenda(dto.getAgenda());
        reservation.setAttendees(dto.getAttendees());
        reservation.setStartDate(LocalDateTime.parse(dto.getStartDate(), formatter));
        reservation.setEndDate(LocalDateTime.parse(dto.getEndDate(), formatter));
        reservation.setRoom(this.roomService.getRoomById(dto.getRoomId()));
    }
}
