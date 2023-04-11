package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne
    private Region toRegion;

    @ManyToOne
    private User user;

    private double fromLatitude;

    private double fromLongitude;

    private double toLongitude;

    private double toLatitude;

    private double frontSeatPrice;

    private double backSeatPrice;

    private boolean baggage;

    private boolean isActive;

    private byte emptySeat;

    private LocalDateTime createdTime;

    private String info;
    public static AnnouncementDriver from(AnnouncementDriverRegisterRequestDto announcementRequestDto, User user, RegionRepository regionRepository) {
        return AnnouncementDriver.builder()
                .user(user)
                .fromRegion(regionRepository.getById(announcementRequestDto.getFromRegionId()))
                .toRegion(regionRepository.getById(announcementRequestDto.getToRegionId()))
                .fromLatitude(announcementRequestDto.getFromLatitude())
                .fromLongitude(announcementRequestDto.getFromLongitude())
                .toLatitude(announcementRequestDto.getToLatitude())
                .toLongitude(announcementRequestDto.getToLongitude())
                .frontSeatPrice(announcementRequestDto.getFrontSeatPrice())
                .backSeatPrice(announcementRequestDto.getBackSeatPrice())
                .baggage(announcementRequestDto.isBaggage())
                .emptySeat(announcementRequestDto.getEmptySeat())
                .info(announcementRequestDto.getInfo())
                .createdTime(LocalDateTime.now())
                .isActive(true)
                .build();

    }
}
