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
     private boolean forFamiliar;

     private String info;

     public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementRequestDto,String url) {
          return AnnouncementPassengerResponse.builder()
              .id(announcementRequestDto.getId())
              .userResponseDto(UserResponseDto.from(announcementRequestDto.getUser(),url))
              .fromRegion(announcementRequestDto.getFromRegion())
              .toRegion(announcementRequestDto.getToRegion())
              .fromLatitude(announcementRequestDto.getFromLatitude())
              .fromLongitude(announcementRequestDto.getFromLongitude())
              .toLatitude(announcementRequestDto.getToLatitude())
              .toLongitude(announcementRequestDto.getToLongitude())
              .baggage(announcementRequestDto.isBaggage())
              .price(announcementRequestDto.getPrice())
              .forFamiliar(announcementRequestDto.isForFamiliar())
              .info(announcementRequestDto.getInfo())
              .build();
     }
}
