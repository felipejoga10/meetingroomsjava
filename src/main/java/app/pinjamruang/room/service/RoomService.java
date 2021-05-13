package app.pinjamruang.room.service;

import app.pinjamruang.room.dto.CreateRoomDto;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RoomService {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    private RoomRepository repository;

    public List<Room> getAllRooms() {
        return this.repository.findAll();
    }

    public Room getRoomById(Long id) {
        Room result = this.repository.getOne(id);
        if (result == null) {
            throw new ResourceNotFoundException(String.format("Can\'t found room with id %d", id));
        }
        return result;
    }

    public Room createRoom(CreateRoomDto roomDto) {
        Room newRoom = createRoomFromDto(roomDto);

        return this.repository.save(newRoom);
    }

    public Room updateRoom(Long roomId, CreateRoomDto roomDto) {
        Room room = getRoomById(roomId);
        updateRoomFromDto(room, roomDto);

        return this.repository.save(room);
    }

    public void deleteRoom(Long roomId) {
        getRoomById(roomId);

        this.repository.deleteById(roomId);
    }

    private Room createRoomFromDto(CreateRoomDto dto) {
        Room room = new Room();
        updateRoomFromDto(room, dto);

        return room;
    }

    private void updateRoomFromDto(Room room, CreateRoomDto dto) {
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setOpenTime(LocalTime.parse(dto.getOpenTime(), formatter));
        room.setCloseTime(LocalTime.parse(dto.getCloseTime(), formatter));
    }
}
