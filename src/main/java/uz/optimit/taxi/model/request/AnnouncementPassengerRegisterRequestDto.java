package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

     private double price;

     private boolean baggage;

     private boolean active;

     private boolean forFamiliar;

     private LocalDateTime timeToTravel;

     private String info;
}
