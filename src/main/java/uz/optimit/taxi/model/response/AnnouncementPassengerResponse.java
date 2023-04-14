package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Region;

import java.util.UUID;

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

     private double price;

     private boolean baggage;
     private int forFamiliar;

     private String info;

     public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementPassenger, String url) {
          return AnnouncementPassengerResponse.builder()
              .id(announcementPassenger.getId())
              .userResponseDto(UserResponseDto.from(announcementPassenger.getUser(),url))
              .fromRegion(announcementPassenger.getFromRegion())
              .toRegion(announcementPassenger.getToRegion())
              .fromLatitude(announcementPassenger.getFromLatitude())
              .fromLongitude(announcementPassenger.getFromLongitude())
              .toLatitude(announcementPassenger.getToLatitude())
              .toLongitude(announcementPassenger.getToLongitude())
              .baggage(announcementPassenger.isBaggage())
              .price(announcementPassenger.getPrice())
              .forFamiliar(announcementPassenger.getForFamily())
              .info(announcementPassenger.getInfo())
              .build();
     }
}
