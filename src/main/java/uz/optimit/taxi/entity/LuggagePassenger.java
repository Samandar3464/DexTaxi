package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.webjars.NotFoundException;
import uz.optimit.taxi.entity.Enum.Constants;
import uz.optimit.taxi.exception.LuggageAnnouncementNotFound;
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

     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

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
     
     @ManyToOne
     private User user;


     public static LuggagePassenger from(LuggagePassengerRequestDto luggagePassengerRequestDto, RegionRepository regionRepository, CityRepository cityRepository, FamiliarRepository familiarRepository,User user) {
          return LuggagePassenger
              .builder()
              .active(true)
              .user(user)
              .createdTime(LocalDateTime.now())
              .price(luggagePassengerRequestDto.getPrice())
              .timeToLeave(luggagePassengerRequestDto.getTimeToLeave())
              .cargoDescription(luggagePassengerRequestDto.getCargoDescription())
              .fromLatitude(luggagePassengerRequestDto.getFromLatitude())
              .fromLongitude(luggagePassengerRequestDto.getFromLongitude())
              .toLatitude(luggagePassengerRequestDto.getToLatitude())
              .toLongitude(luggagePassengerRequestDto.getToLongitude())
              .fromRegion(regionRepository.findById(luggagePassengerRequestDto.getFromRegionId()).orElseThrow(()-> new NotFoundException(Constants.REGION_NOT_FOUND)))
              .toRegion(regionRepository.findById(luggagePassengerRequestDto.getToRegionId()).orElseThrow(()-> new NotFoundException(Constants.REGION_NOT_FOUND)))
              .fromCity(cityRepository.findById(luggagePassengerRequestDto.getFromCityId()).orElseThrow(()-> new NotFoundException(Constants.CITY_NOT_FOUND)))
              .toCity(cityRepository.findById(luggagePassengerRequestDto.getToCityId()).orElseThrow(()-> new NotFoundException(Constants.CITY_NOT_FOUND)))
              .sender(familiarRepository.findById(luggagePassengerRequestDto.getSenderId()).orElseThrow(()-> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND)))
              .receiver(familiarRepository.findById(luggagePassengerRequestDto.getReceiverId()).orElseThrow(()-> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND)))
              .build();
     }
}
