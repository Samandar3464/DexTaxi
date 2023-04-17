package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPassengerRegisterRequestDto {

     private Integer fromRegionId;
     private Integer toRegionId;
     private Integer fromCityId;
     private Integer toCityId;
     private double fromLatitude;
     private double fromLongitude;
     private double toLongitude;
     private double toLatitude;
     private boolean baggage;
     private List<UUID> passengersList;
     private LocalDateTime timeToTravel;
     private String info;
}
