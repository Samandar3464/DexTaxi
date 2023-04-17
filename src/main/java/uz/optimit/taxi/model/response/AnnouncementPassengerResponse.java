package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;

import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementPassengerResponse {
     private UUID id;
     private Region fromRegion;
     private Region toRegion;
     private City fromCity;
     private City toCity;
     private UserResponseDto userResponseDto;
     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

     private List<Familiar> passengersList;
     private boolean baggage;

     private String info;

     public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementPassenger, String url) {
          return AnnouncementPassengerResponse.builder()
              .id(announcementPassenger.getId())
              .userResponseDto(UserResponseDto.from(announcementPassenger.getUser(), url))
              .fromRegion(announcementPassenger.getFromRegion())
              .toRegion(announcementPassenger.getToRegion())
              .fromLatitude(announcementPassenger.getFromLatitude())
              .fromLongitude(announcementPassenger.getFromLongitude())
              .toLatitude(announcementPassenger.getToLatitude())
              .toLongitude(announcementPassenger.getToLongitude())
              .baggage(announcementPassenger.isBaggage())
              .passengersList(announcementPassenger.getPassengersList())
              .info(announcementPassenger.getInfo())
              .build();
     }
}
