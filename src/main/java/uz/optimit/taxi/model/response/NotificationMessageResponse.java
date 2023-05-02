package uz.optimit.taxi.model.response;

import lombok.*;
import uz.optimit.taxi.model.request.LuggageNotificationRequestDto;
import uz.optimit.taxi.model.request.NotificationRequestDto;

import java.util.Map;

import static uz.optimit.taxi.entity.Enum.Constants.CAR_HAS_ENOUGH_SEAT_BUT_NOT_SUIT_YOUR_CHOOSE;
import static uz.optimit.taxi.entity.Enum.Constants.YOU_HAVE_RECEIVED_A_MESSAGE_FROM_A_DRIVER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationMessageResponse {

    private String receiverToken;

    private String title;

    private String body;

    private Map<String, String> data;

    public static NotificationMessageResponse reCreate(String token) {

        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(YOU_HAVE_RECEIVED_A_MESSAGE_FROM_A_DRIVER)
                .body(CAR_HAS_ENOUGH_SEAT_BUT_NOT_SUIT_YOUR_CHOOSE)
                .build();

    }

    public static NotificationMessageResponse fromForPassenger(NotificationRequestDto notificationRequestDto,String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .build();
    }
    public static NotificationMessageResponse fromForPassenger(LuggageNotificationRequestDto notificationRequestDto,String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .build();
    }

    public static NotificationMessageResponse fromForDriver(NotificationRequestDto notificationRequestDto, String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .build();
    }
    public static NotificationMessageResponse fromForDriver(LuggageNotificationRequestDto notificationRequestDto, String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .build();
    }
}
