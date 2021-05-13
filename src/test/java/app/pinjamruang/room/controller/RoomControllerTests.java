package app.pinjamruang.room.controller;

import app.pinjamruang.room.dto.CreateRoomDto;
import app.pinjamruang.room.model.Room;
import app.pinjamruang.room.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static app.pinjamruang.TestUtils.createDummyRoom;
import static app.pinjamruang.TestUtils.createDummyRoomDto;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RoomController.class)
public class RoomControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService service;

    @Test
    public void getAllRooms_emptyRepository_success() throws Exception {
        when(service.getAllRooms()).thenReturn(new ArrayList<>());

        mvc.perform(get("/rooms/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(service).getAllRooms();
    }

    @Test
    public void getAllRooms_notEmptyRepository_success() throws Exception {
        List<Room> rooms = new ArrayList<>();
        rooms.add(createDummyRoom());
        rooms.add(createDummyRoom());

        when(service.getAllRooms()).thenReturn(rooms);

        mvc.perform(get("/rooms/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(rooms.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(rooms.get(1).getName())));

        verify(service).getAllRooms();
    }

    @Test
    public void getRoomById_roomNotFound_throwsResourceNotFoundException() throws Exception {
        when(service.getRoomById(1L)).thenThrow(new ResourceNotFoundException());

        mvc.perform(get("/rooms/1"))
                .andExpect(status().isNotFound());

        verify(service).getRoomById(1L);
    }

    @Test
    public void getRoomById_success() throws Exception {
        Room room = createDummyRoom();
        when(service.getRoomById(1L)).thenReturn(room);

        mvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.capacity", is(room.getCapacity())))
                .andExpect(jsonPath("$.openTime", is(room.getOpenTime().format(DateTimeFormatter.ISO_LOCAL_TIME))))
                .andExpect(jsonPath("$.closeTime", is(room.getCloseTime().format(DateTimeFormatter.ISO_LOCAL_TIME))));

        verify(service).getRoomById(1L);
    }

    @Test
    public void createRoom_success() throws Exception {
        Room room = createDummyRoom();
        when(service.createRoom(any(CreateRoomDto.class))).thenReturn(room);

        mvc.perform(
                post("/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDummyRoomDto()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.capacity", is(room.getCapacity())))
                .andExpect(jsonPath("$.openTime", is(room.getOpenTime().format(DateTimeFormatter.ISO_LOCAL_TIME))))
                .andExpect(jsonPath("$.closeTime", is(room.getCloseTime().format(DateTimeFormatter.ISO_LOCAL_TIME))));

        verify(service).createRoom(ArgumentMatchers.any(CreateRoomDto.class));
    }

    @Test
    public void updateRoom_success() throws Exception {
        Room room = createDummyRoom();
        when(service.updateRoom(ArgumentMatchers.eq(1L), ArgumentMatchers.any(CreateRoomDto.class))).thenReturn(room);

        mvc.perform(
                put("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDummyRoomDto()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(room.getName())))
                .andExpect(jsonPath("$.capacity", is(room.getCapacity())))
                .andExpect(jsonPath("$.openTime", is(room.getOpenTime().format(DateTimeFormatter.ISO_LOCAL_TIME))))
                .andExpect(jsonPath("$.closeTime", is(room.getCloseTime().format(DateTimeFormatter.ISO_LOCAL_TIME))));

        verify(service).updateRoom(ArgumentMatchers.eq(1L), ArgumentMatchers.any(CreateRoomDto.class));
    }

    @Test
    public void updateRoom_roomNotFound_throwsResourceNotFoundException() throws Exception {
        when(service.updateRoom(ArgumentMatchers.eq(1L), ArgumentMatchers.any(CreateRoomDto.class))).thenThrow(new ResourceNotFoundException());

        mvc.perform(
                put("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDummyRoomDto()))
        )
                .andExpect(status().isNotFound());

        verify(service).updateRoom(ArgumentMatchers.eq(1L), ArgumentMatchers.any(CreateRoomDto.class));
    }

    @Test
    public void deleteRoom_success() throws Exception {
        mvc.perform(delete("/rooms/1"))
                .andExpect(status().isOk());

        verify(service).deleteRoom(1L);
    }

    @Test
    public void deleteRoom_roomNotFound_throwsResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException()).when(service).deleteRoom(1L);

        mvc.perform(delete("/rooms/1"))
                .andExpect(status().isNotFound());

        verify(service).deleteRoom(1L);
    }

}