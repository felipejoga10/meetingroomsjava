package app.pinjamruang.room.service;

import app.pinjamruang.room.dto.CreateRoomDto;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import static org.mockito.Mockito.*;

public class RoomServiceTests {
    @InjectMocks
    RoomService service;

    @Mock
    private RoomRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllRooms_success() {
        service.getAllRooms();

        verify(repository, times(1)).findAll();
    }

    @Test
    public void getRoomById_success() {
        Room room = new Room();
        when(repository.getOne(1L)).thenReturn(room);

        service.getRoomById(1L);

        verify(repository).getOne(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getRoomById_roomNotFound_throwsResourceNotFoundException() {
        when(repository.getOne(1L)).thenThrow(new ResourceNotFoundException());

        service.getRoomById(1L);
    }

    @Test
    public void createRoom_success() {
        Room room = new Room();

        when(repository.save(any(Room.class))).thenReturn(room);

        CreateRoomDto dto = new CreateRoomDto("Room 1", 10, "09:00", "10:00");

        service.createRoom(dto);

        verify(repository).save(any(Room.class));
    }

    @Test
    public void updateRoom_success() {
        Room room = new Room();

        when(repository.getOne(1L)).thenReturn(room);
        when(repository.save(any(Room.class))).thenReturn(room);

        CreateRoomDto dto = new CreateRoomDto("Room 1", 10, "09:00", "10:00");

        service.updateRoom(1L, dto);

        verify(repository).save(any(Room.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateRoom_roomNotFound_throwsResourceNotFoundException() {
        when(repository.getOne(1L)).thenThrow(new ResourceNotFoundException());

        CreateRoomDto dto = new CreateRoomDto("Room 1", 10, "09:00", "10:00");

        service.updateRoom(1L, dto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteRoom_roomNotFound_throwsResourceNotFoundException() {
        when(repository.getOne(1L)).thenThrow(new ResourceNotFoundException());

        service.deleteRoom(1L);
    }

    @Test
    public void deleteRoom_success() {
        Room room = new Room();
        when(repository.getOne(1L)).thenReturn(room);
        service.deleteRoom(1L);

        verify(repository).deleteById(1L);
    }
}
