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
     private Region fromRegion;
     private Region toRegion;

     public  static AnnouncementDriverResponseAnonymous from(AnnouncementDriver announcementDriver) {
          return AnnouncementDriverResponseAnonymous
              .builder()
              .id(announcementDriver.getId())
              .fromRegion(announcementDriver.getFromRegion())
              .toRegion(announcementDriver.getToRegion())
              .build();
     }
}
