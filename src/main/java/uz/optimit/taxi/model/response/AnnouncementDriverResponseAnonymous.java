package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Region;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponseAnonymous {
     private UUID id;
     private RegionResponseDto fromRegion;
     private RegionResponseDto toRegion;
     private double frontSeatPrice;
     private double backSeatPrice;
     private LocalDateTime timeToDrive;

     public  static AnnouncementDriverResponseAnonymous from(AnnouncementDriver announcementDriver) {
          return AnnouncementDriverResponseAnonymous
              .builder()
              .id(announcementDriver.getId())
              .fromRegion(RegionResponseDto.from(announcementDriver.getFromRegion()))
              .toRegion(RegionResponseDto.from(announcementDriver.getToRegion()))
              .frontSeatPrice(announcementDriver.getFrontSeatPrice())
              .backSeatPrice(announcementDriver.getBackSeatPrice())
              .timeToDrive(announcementDriver.getTimeToDrive())
              .build();
     }
}
