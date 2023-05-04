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
    private City toCity;
    private UUID userId;
    private String info;
    private double price;
    private City fromCity;
    private boolean baggage;
    private double toLatitude;
    private double toLongitude;
    private double fromLatitude;
    private double fromLongitude;
    private String announcementOwnerPhone;
    private RegionResponseDto toRegion;
    private RegionResponseDto fromRegion;
    private List<Familiar> passengersList;
    private String timeToTravel;

    public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementPassenger) {
        return AnnouncementPassengerResponse.builder()
                .id(announcementPassenger.getId())
                .userId(announcementPassenger.getUser().getId())
                .fromRegion(RegionResponseDto.from(announcementPassenger.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementPassenger.getToRegion()))
                .fromCity(announcementPassenger.getFromCity())
                .toCity(announcementPassenger.getToCity())
                .price(announcementPassenger.getPrice())
                .fromLatitude(announcementPassenger.getFromLatitude())
                .fromLongitude(announcementPassenger.getFromLongitude())
                .toLatitude(announcementPassenger.getToLatitude())
                .toLongitude(announcementPassenger.getToLongitude())
                .baggage(announcementPassenger.isBaggage())
                .passengersList(announcementPassenger.getPassengersList())
                .info(announcementPassenger.getInfo())
                .announcementOwnerPhone(announcementPassenger.getUser().getPhone())
                .timeToTravel(announcementPassenger.getTimeToTravel().toString())
                .build();
    }
}
