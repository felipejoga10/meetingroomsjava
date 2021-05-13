package app.pinjamruang.reservation.service;

import app.pinjamruang.reservation.dto.CreateReservationDto;
import app.pinjamruang.reservation.exception.RoomNotAvailableException;
import app.pinjamruang.reservation.model.Reservation;
import app.pinjamruang.reservation.repository.ReservationRepository;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static app.pinjamruang.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReservationServiceTests {
    @InjectMocks
    ReservationService service;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    RoomService roomService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getAllReservations_success() {
        service.getAllReservations();

        verify(reservationRepository).findAll();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getReservationById_idNotFound_throwsResourceNotFoundException() {
        Reservation reservation = new Reservation();
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        service.getReservationById(2L);
    }

    @Test
    public void getReservationById_success() {
        Reservation reservation = new Reservation();
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        service.getReservationById(1L);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void createReservation_notEnoughCapacity_throwsRoomNotAvailableException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 09:00",
                "2019-06-12 09:10",
                100,
                "Testing",
                1L
        );

        service.createReservation(dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void createReservation_roomNotOnOperationalTime_throwsRoomNotAvailableException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 09:00",
                "2019-06-12 09:10",
                10,
                "Testing",
                1L
        );

        service.createReservation(dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void createReservation_roomIsAlreadyReserved_throwsRoomNotAvailableException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 09:00",
                "2019-06-12 09:10",
                10,
                "Testing",
                1L
        );
        LocalDateTime startDate = LocalDateTime.parse("2019-06-12 09:00", DTO_DATETIME_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse("2019-06-12 09:10", DTO_DATETIME_FORMATTER);

        List<Reservation> dummy = new ArrayList<>();
        dummy.add(new Reservation());

        when(
                reservationRepository.findByStartDateBetweenAndEndDateBetween(eq(startDate), eq(endDate), eq(startDate), eq(endDate))
        ).thenReturn(dummy);

        service.createReservation(dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void createReservation_reservationWithinOneDay_throwsRoomNotAvailableException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 09:00",
                "2019-06-13 09:00",
                10,
                "Testing",
                1L
        );

        service.createReservation(dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test
    public void createReservation_success() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 10:00",
                "2019-06-12 12:00",
                10,
                "Testing",
                1L
        );

        LocalDateTime startDate = LocalDateTime.parse("2019-06-12 10:00", DTO_DATETIME_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse("2019-06-12 12:00", DTO_DATETIME_FORMATTER);

        Reservation dummy = new Reservation(startDate, endDate, 10, "Testing", room);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(dummy);

        service.createReservation(dto);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test(expected = RoomNotAvailableException.class)
    public void updateReservation_notEnoughCapacity_throwsResourceNotFoundException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        Reservation reservation = createDummyReservation(room);
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 10:00",
                "2019-06-12 12:00",
                100,
                "Testing",
                1L
        );

        service.updateReservation(1L, dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void updateReservation_roomNotOnOperationalTime_throwsResourceNotFoundException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        Reservation reservation = createDummyReservation(room);
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 01:00",
                "2019-06-12 02:00",
                10,
                "Testing",
                1L
        );

        service.updateReservation(1L, dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void updateReservation_roomIsAlreadyReserved_throwsResourceNotFoundException() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        Reservation reservation = createDummyReservation(room);
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 09:00",
                "2019-06-12 09:10",
                10,
                "Testing",
                1L
        );

        LocalDateTime startDate = LocalDateTime.parse("2019-06-12 09:00", DTO_DATETIME_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse("2019-06-12 09:10", DTO_DATETIME_FORMATTER);

        List<Reservation> dummy = new ArrayList<>();
        dummy.add(new Reservation());

        when(
                reservationRepository.findByStartDateBetweenAndEndDateBetween(eq(startDate), eq(endDate), eq(startDate), eq(endDate))
        ).thenReturn(dummy);

        service.updateReservation(1L, dto);

        verifyZeroInteractions(reservationRepository);
    }

    @Test
    public void updateReservation_success() {
        Room room = createDummyRoom();
        when(roomService.getRoomById(1L)).thenReturn(room);

        Reservation reservation = createDummyReservation(room);
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        CreateReservationDto dto = new CreateReservationDto(
                "2019-06-12 10:00",
                "2019-06-12 12:00",
                10,
                "Testing",
                1L
        );

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        service.updateReservation(1L, dto);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteReservation_reservationNotExist_throwsResourceNotFoundException() {
        when(reservationRepository.getOne(1L)).thenReturn(null);

        service.deleteReservation(1L);

        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    public void deleteReservation_success() {
        Reservation reservation = createDummyReservation(createDummyRoom());
        when(reservationRepository.getOne(1L)).thenReturn(reservation);

        service.deleteReservation(1L);

        verify(reservationRepository).deleteById(1L);
    }
}
