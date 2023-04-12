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

    private double frontSeatPrice;

    private double backSeatPrice;

    private boolean baggage;

    private boolean active;

    private byte emptySeat;

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
                .emptySeat(announcementRequestDto.getEmptySeat())
                .timeToDrive(announcementRequestDto.getTimeToDrive())
                .info(announcementRequestDto.getInfo())
                .createdTime(LocalDateTime.now())
                .active(true)
                .build();
    }
}
