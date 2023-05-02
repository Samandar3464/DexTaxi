package uz.optimit.taxi.model.response;

import lombok.*;
import uz.optimit.taxi.entity.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuggageDriverResponse {

     private UUID id;

     private String cargoDescription;

     private LocalDateTime timeToLeave;

     private RegionResponseDto fromRegion;

     private RegionResponseDto toRegion;

     private City fromCity;

     private City toCity;

     private UserResponseDto supplier;

     public static LuggageDriverResponse from(LuggageDriver luggageDriver,String url){
          return LuggageDriverResponse
              .builder()
              .id(luggageDriver.getId())
              .cargoDescription(luggageDriver.getCargoDescription())
              .timeToLeave(luggageDriver.getTimeToLeave())
              .fromRegion(RegionResponseDto.from(luggageDriver.getFromRegion()))
              .toRegion(RegionResponseDto.from(luggageDriver.getToRegion()))
              .fromCity(luggageDriver.getFromCity())
              .toCity(luggageDriver.getToCity())
              .supplier(UserResponseDto.fromAnnouncement(luggageDriver.getSupplier(),url))
              .build();
     }
}
