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

    private UUID announcementId;

    private boolean active;

    private boolean received;

    private LocalDateTime createdTime;

    @ManyToOne
    @JsonIgnore
    private User user;

    public static Notification from(NotificationRequestDto notificationRequestDto , User user){
        return Notification.builder()
                .senderId(user.getId())
                .user(user)
                .receiverId(notificationRequestDto.getReceiverId())
                .announcementId(notificationRequestDto.getAnnouncementId())
                .createdTime(LocalDateTime.now())
                .received(false)
                .active(true)
                .build();

    }
}
