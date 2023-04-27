package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Region;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPassengerResponseAnonymous {
    private UUID id;
    private RegionResponseDto fromRegion;
    private RegionResponseDto toRegion;
    private City fromCity;
    private City toCity;
    private double price;
    private LocalDateTime timeToTravel;

    public static AnnouncementPassengerResponseAnonymous from(AnnouncementPassenger announcementPassenger) {
        return AnnouncementPassengerResponseAnonymous
                .builder()
                .id(announcementPassenger.getId())
                .fromRegion(RegionResponseDto.from(announcementPassenger.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementPassenger.getToRegion()))
                .fromCity(announcementPassenger.getFromCity())
                .toCity(announcementPassenger.getToCity())
                .price(announcementPassenger.getPrice())
                .timeToTravel(announcementPassenger.getTimeToTravel())
                .build();
    }
}
