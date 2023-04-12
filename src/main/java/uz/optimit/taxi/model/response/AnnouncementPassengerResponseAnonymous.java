package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.Region;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPassengerResponseAnonymous {
     private UUID id;
     private Region fromRegion;
     private Region toRegion;

     public  static AnnouncementPassengerResponseAnonymous from(AnnouncementPassenger announcementPassenger) {
          return AnnouncementPassengerResponseAnonymous
              .builder()
              .id(announcementPassenger.getId())
              .fromRegion(announcementPassenger.getFromRegion())
              .toRegion(announcementPassenger.getToRegion())
              .build();
     }
}