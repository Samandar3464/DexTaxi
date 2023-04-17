package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.repository.CarRepository;
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
public class AnnouncementDriver {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

     @ManyToOne
     private Region fromRegion;
     private double frontSeatPrice;
     private double backSeatPrice;
     @ManyToOne
     private Region toRegion;

     @ManyToOne
     private User user;

     @ManyToOne
     private Car car;

     private boolean baggage;

     private boolean active;

     private LocalDateTime timeToDrive;

     private LocalDateTime createdTime;

     private String info;

     public static AnnouncementDriver from(AnnouncementDriverRegisterRequestDto announcementRequestDto, User user, RegionRepository regionRepository) {
          return AnnouncementDriver.builder()
              .user(user)
              .fromRegion(regionRepository.getById(announcementRequestDto.getFromRegionId()))
              .toRegion(regionRepository.getById(announcementRequestDto.getToRegionId()))
              .frontSeatPrice(announcementRequestDto.getFrontSeatPrice())
              .backSeatPrice(announcementRequestDto.getBackSeatPrice())
              .baggage(announcementRequestDto.isBaggage())
              .timeToDrive(announcementRequestDto.getTimeToDrive())
              .info(announcementRequestDto.getInfo())
              .createdTime(LocalDateTime.now())
              .active(true)
              .build();
     }
}
