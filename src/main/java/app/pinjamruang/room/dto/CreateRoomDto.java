package app.pinjamruang.room.dto;

public class CreateRoomDto {
    private String name;
    private Integer capacity;
    private String openTime;
    private String closeTime;

    public CreateRoomDto(String name, Integer capacity, String openTime, String closeTime) {
        this.name = name;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }
}

