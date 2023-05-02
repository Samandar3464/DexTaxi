package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponse {
    private UUID id;
    private RegionResponseDto fromRegion;
    private RegionResponseDto toRegion;
    private City fromCity;
    private City toCity;
    private UserResponseDto userResponseDto;
    private double frontSeatPrice;
    private double backSeatPrice;
    private String info;
    private boolean baggage;
    private List<String> carPhotoPath;
    private String color;
    private String carNumber;
    private String autoModel;
    private LocalDateTime timeToDrive;
    private List<Seat> seatList;
    private List<Familiar> familiars;

    public static AnnouncementDriverResponse from(AnnouncementDriver announcementDriver, Car car, List<Familiar> familiars,String downloadUrl) {

        List<Attachment> attachment1 = car.getAutoPhotos();
        List<String> photos = new ArrayList<>();
        attachment1.forEach(attachment -> {
            photos.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
        });

        if (announcementDriver.getFromCity() == null) {
            return getResponse1(announcementDriver, car, downloadUrl, familiars,photos);
        }
        return getResponse(announcementDriver, car, downloadUrl,familiars, photos);
    }

    public static AnnouncementDriverResponse fromDriver(AnnouncementDriver announcementDriver, Car car,String downloadUrl) {

        List<Attachment> attachment1 = car.getAutoPhotos();
        List<String> photos = new ArrayList<>();
        attachment1.forEach(attachment -> {
            photos.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
        });

        if (announcementDriver.getFromCity() == null) {
            return getResponse1(announcementDriver, car, downloadUrl, null,photos);
        }
        return getResponse(announcementDriver, car, downloadUrl,null, photos);
    }

    private static AnnouncementDriverResponse getResponse(AnnouncementDriver announcementDriver, Car car, String downloadUrl, List<Familiar> familiars, List<String> photos) {
        return AnnouncementDriverResponse
                .builder()
                .id(announcementDriver.getId())
                .fromRegion(RegionResponseDto.from(announcementDriver.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementDriver.getToRegion()))
                .fromCity(announcementDriver.getFromCity())
                .toCity(announcementDriver.getToCity())
                .familiars(familiars)
                .userResponseDto(UserResponseDto.fromAnnouncement(announcementDriver.getUser(), downloadUrl))
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .info(announcementDriver.getInfo())
                .baggage(announcementDriver.isBaggage())
                .timeToDrive(announcementDriver.getTimeToDrive())
                .carPhotoPath(photos)
                .color(car.getColor())
                .seatList(announcementDriver.getCar().getSeatList())
                .carNumber(car.getCarNumber())
                .autoModel(car.getAutoModel().getName())
                .build();
    }

    private static AnnouncementDriverResponse getResponse1(AnnouncementDriver announcementDriver, Car car, String downloadUrl, List<Familiar> familiars, List<String> photos) {
        return AnnouncementDriverResponse
                .builder()
                .id(announcementDriver.getId())
                .fromRegion(RegionResponseDto.from(announcementDriver.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementDriver.getToRegion()))
                .familiars(familiars)
                .userResponseDto(UserResponseDto.fromAnnouncement(announcementDriver.getUser(), downloadUrl))
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .info(announcementDriver.getInfo())
                .baggage(announcementDriver.isBaggage())
                .timeToDrive(announcementDriver.getTimeToDrive())
                .carPhotoPath(photos)
                .color(car.getColor())
                .seatList(announcementDriver.getCar().getSeatList())
                .carNumber(car.getCarNumber())
                .autoModel(car.getAutoModel().getName())
                .build();
    }


}
