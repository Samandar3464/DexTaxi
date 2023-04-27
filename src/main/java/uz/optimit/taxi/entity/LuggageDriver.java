package uz.optimit.taxi.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.LuggageDriverRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LuggageDriver {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

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
     @JsonBackReference
     private User supplier;


     public static LuggageDriver from(LuggageDriverRequestDto luggageDriverRequestDto, RegionRepository regionRepository, CityRepository cityRepository, User user) {
          return LuggageDriver
              .builder()
              .cargoDescription(luggageDriverRequestDto.getCargoDescription())
              .timeToLeave(luggageDriverRequestDto.getTimeToLeave())
              .createdTime(LocalDateTime.now())
              .fromRegion(regionRepository.findById(luggageDriverRequestDto.getFromRegionId()).get())
              .toRegion(regionRepository.findById(luggageDriverRequestDto.getToRegionId()).get())
              .fromCity(cityRepository.findById(luggageDriverRequestDto.getFromCityId()).get())
              .toCity(cityRepository.findById(luggageDriverRequestDto.getToCityId()).get())
              .supplier(user)
              .active(true)
              .build();
     }

}
