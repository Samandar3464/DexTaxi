package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.LuggagePassengerRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LuggagePassenger {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

     private double price;

     private String cargoDescription;

     private LocalDateTime timeToLeave;

     private LocalDateTime createdTime;

     private boolean active;

     @ManyToOne
     private Region fromRegion;

     @ManyToOne
     private Region toRegion;

     @ManyToOne
     private City fromCity;

     @ManyToOne
     private City toCity;

     @ManyToOne
     private Familiar sender;

     @ManyToOne
     private Familiar receiver;

     @ManyToOne
     private User supplier;


     public static LuggagePassenger from(LuggagePassengerRequestDto luggagePassengerRequestDto, RegionRepository regionRepository, CityRepository cityRepository, FamiliarRepository familiarRepository) {
          return LuggagePassenger
              .builder()
              .active(true)
              .createdTime(LocalDateTime.now())
              .price(luggagePassengerRequestDto.getPrice())
              .timeToLeave(luggagePassengerRequestDto.getTimeToLeave())
              .cargoDescription(luggagePassengerRequestDto.getCargoDescription())
              .fromRegion(regionRepository.findById(luggagePassengerRequestDto.getFromRegionId()).get())
              .toRegion(regionRepository.findById(luggagePassengerRequestDto.getToRegionId()).get())
              .fromCity(cityRepository.findById(luggagePassengerRequestDto.getFromCityId()).get())
              .toCity(cityRepository.findById(luggagePassengerRequestDto.getToCityId()).get())
              .sender(familiarRepository.findById(luggagePassengerRequestDto.getSenderId()).get())
              .receiver(familiarRepository.findById(luggagePassengerRequestDto.getReceiverId()).get())
              .build();
     }
}
