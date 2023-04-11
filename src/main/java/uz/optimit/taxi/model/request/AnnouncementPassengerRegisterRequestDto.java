package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPassengerRegisterRequestDto {

     private Integer fromRegionId;

     private Integer toRegionId;

     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

     private double price;

     private boolean baggage;

     private boolean isActive;

     private boolean forFamiliar;

     private String info;
}
