package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Familiar;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementPassengerResponse {
    private UUID id;
    private RegionResponseDto fromRegion;
    private RegionResponseDto toRegion;
    private City fromCity;
    private City toCity;
    private UUID userId;
    private double fromLatitude;

    private double fromLongitude;

    private double toLongitude;

    private double toLatitude;

    private List<Familiar> passengersList;

    private boolean baggage;

    private String info;

    public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementPassenger) {
        return AnnouncementPassengerResponse.builder()
                .id(announcementPassenger.getId())
                .userId(announcementPassenger.getUser().getId())
                .fromRegion(RegionResponseDto.from(announcementPassenger.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementPassenger.getToRegion()))
                .fromCity(announcementPassenger.getFromCity())
                .toCity(announcementPassenger.getToCity())
                .fromLatitude(announcementPassenger.getFromLatitude())
                .fromLongitude(announcementPassenger.getFromLongitude())
                .toLatitude(announcementPassenger.getToLatitude())
                .toLongitude(announcementPassenger.getToLongitude())
                .baggage(announcementPassenger.isBaggage())
                .passengersList(announcementPassenger.getPassengersList())
                .info(announcementPassenger.getInfo())
                .build();
    }
}
