package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {

    private String receiverToken;

    private String title;

    private String body;

    private UUID receiverId;

    private UUID announcementDriverId;

    private UUID announcementPassengerId;

    private List<UUID> seatIdList;
}
