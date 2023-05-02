package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LuggagePassengerRequestDto {

     private double price;

     private String cargoDescription;

     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

     private LocalDateTime timeToLeave;

     private Integer fromRegionId;

     private Integer toRegionId;

     private Integer fromCityId;

     private Integer toCityId;

     private UUID senderId;

     private UUID receiverId;
}
