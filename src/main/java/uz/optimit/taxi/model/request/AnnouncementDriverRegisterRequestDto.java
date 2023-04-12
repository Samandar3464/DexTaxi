package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDriverRegisterRequestDto {

     private Integer fromRegionId;

     private Integer toRegionId;

     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

     private double frontSeatPrice;

     private double backSeatPrice;

     private LocalDateTime timeToDrive;

     private boolean baggage;

     private byte emptySeat;

     private String info;
}
