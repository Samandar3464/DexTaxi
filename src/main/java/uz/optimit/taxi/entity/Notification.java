package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.NotificationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID senderId;

    private UUID receiverId;

    private UUID announcementDriverId;

    private UUID announcementPassengerId;

    private boolean active;

    private boolean received;

    private LocalDateTime createdTime;

    @ManyToMany
    private List<Seat> carSeats;

    @ManyToOne
    @JsonIgnore
    private User user;

    public static Notification from(NotificationRequestDto notificationRequestDto ){
        return Notification.builder()
                .receiverId(notificationRequestDto.getReceiverId())
                .announcementDriverId(notificationRequestDto.getAnnouncementDriverId())
                .announcementPassengerId(notificationRequestDto.getAnnouncementPassengerId())
                .createdTime(LocalDateTime.now())
                .received(false)
                .active(true)
                .build();

    }
}
