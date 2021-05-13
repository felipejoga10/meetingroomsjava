package app.pinjamruang.reservation.dto;

public class CreateReservationDto {
    private String startDate;
    private String endDate;
    private Integer attendees;
    private String agenda;
    private Long roomId;

    public CreateReservationDto(String startDate, String endDate, Integer attendees, String agenda, Long roomId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
        this.agenda = agenda;
        this.roomId = roomId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getAttendees() {
        return attendees;
    }

    public String getAgenda() {
        return agenda;
    }

    public Long getRoomId() {
        return roomId;
    }
}
