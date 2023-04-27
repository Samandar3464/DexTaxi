package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuggagePassengerResponse {
     private UUID id;

     private String cargoDescription;

     private double price;

     private LocalDateTime timeToLeave;

     private RegionResponseDto fromRegion;

     private RegionResponseDto toRegion;

     private City fromCity;

     private City toCity;

     private Familiar sender;

     private Familiar receiver;

     public static LuggagePassengerResponse from(LuggagePassenger luggagePassenger){
          return LuggagePassengerResponse
              .builder()
              .id(luggagePassenger.getId())
              .price(luggagePassenger.getPrice())
              .cargoDescription(luggagePassenger.getCargoDescription())
              .timeToLeave(luggagePassenger.getTimeToLeave())
              .fromRegion(RegionResponseDto.from(luggagePassenger.getFromRegion()))
              .toRegion(RegionResponseDto.from(luggagePassenger.getToRegion()))
              .fromCity(luggagePassenger.getFromCity())
              .toCity(luggagePassenger.getToCity())
              .sender(luggagePassenger.getSender())
              .receiver(luggagePassenger.getReceiver())
              .build();
     }
}
