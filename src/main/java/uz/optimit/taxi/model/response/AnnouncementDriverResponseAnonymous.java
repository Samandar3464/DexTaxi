package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Region;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponseAnonymous {
     private UUID id;
     private RegionResponseDto fromRegion;
     private RegionResponseDto toRegion;

     public  static AnnouncementDriverResponseAnonymous from(AnnouncementDriver announcementDriver) {
          return AnnouncementDriverResponseAnonymous
              .builder()
              .id(announcementDriver.getId())
              .fromRegion(RegionResponseDto.from(announcementDriver.getFromRegion()))
              .toRegion(RegionResponseDto.from(announcementDriver.getToRegion()))
              .build();
     }
}
