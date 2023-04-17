package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AnnouncementPassenger {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;
     @ManyToOne
     private Region fromRegion;
     @ManyToOne
     private Region toRegion;

     @ManyToOne
     private City fromCity;
     @ManyToOne
     private City toCity;

     @ManyToOne
     private User user;

     private double fromLatitude;

     private double fromLongitude;

     private double toLongitude;

     private double toLatitude;

     @OneToMany(mappedBy = "announcementPassenger")
     private List<Familiar> passengersList;

     private boolean baggage;

     private boolean active;

     private LocalDateTime timeToTravel;

     private String info;

     private LocalDateTime createdTime;

     public static AnnouncementPassenger from(AnnouncementPassengerRegisterRequestDto announcementRequestDto, User user, RegionRepository regionRepository, CityRepository cityRepository, FamiliarRepository familiarRepository) {
          return AnnouncementPassenger.builder()
              .user(user)
              .fromRegion(regionRepository.getById(announcementRequestDto.getFromRegionId()))
              .toRegion(regionRepository.getById(announcementRequestDto.getToRegionId()))
              .fromCity(cityRepository.getById(announcementRequestDto.getFromCityId()))
              .toCity(cityRepository.getById(announcementRequestDto.getToCityId()))
              .fromLatitude(announcementRequestDto.getFromLatitude())
              .fromLongitude(announcementRequestDto.getFromLongitude())
              .toLatitude(announcementRequestDto.getToLatitude())
              .toLongitude(announcementRequestDto.getToLongitude())
              .passengersList(familiarRepository.findByIdIn(announcementRequestDto.getPassengersList()))
              .timeToTravel(announcementRequestDto.getTimeToTravel())
              .info(announcementRequestDto.getInfo())
              .createdTime(LocalDateTime.now())
              .active(true)
              .build();
     }
}
