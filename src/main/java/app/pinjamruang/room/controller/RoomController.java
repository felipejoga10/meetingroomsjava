package app.pinjamruang.room.controller;

import app.pinjamruang.room.dto.CreateRoomDto;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService service;

    @GetMapping("/")
    public List<Room> getAllRooms() {
        return this.service.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable Long roomId) {
        return this.service.getRoomById(roomId);
    }

    @PostMapping("/")
    public Room createRoom(@Valid @RequestBody CreateRoomDto newRoomDto) {
        return this.service.createRoom(newRoomDto);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @Valid @RequestBody CreateRoomDto newRoomDto) {
        return this.service.updateRoom(roomId, newRoomDto);
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        this.service.deleteRoom(roomId);
    }
}