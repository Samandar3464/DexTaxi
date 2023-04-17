package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponse {
     private UUID id;
     private Region fromRegion;
     private Region toRegion;
     private UserResponseDto userResponseDto;
     private double frontSeatPrice;
     private double backSeatPrice;
     private byte emptySeat;
     private String info;
     private boolean baggage;
     private List<String> carPhotoPath;
     private String color;
     private String carNumber;
     private String autoModel;


     public static AnnouncementDriverResponse from(AnnouncementDriver announcementDriver, Car car,String downloadUrl){

          List<Attachment> attachment1 = car.getAutoPhotos();
          List<String> photos = new ArrayList<>();
          attachment1.forEach(attachment ->{
               photos.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
          });


          return AnnouncementDriverResponse
              .builder()
              .id(announcementDriver.getId())
              .fromRegion(announcementDriver.getFromRegion())
              .toRegion(announcementDriver.getToRegion())
              .userResponseDto(UserResponseDto.from(announcementDriver.getUser(),downloadUrl))
              .frontSeatPrice(announcementDriver.getFrontSeatPrice())
              .backSeatPrice(announcementDriver.getBackSeatPrice())
              .info(announcementDriver.getInfo())
              .baggage(announcementDriver.isBaggage())
              .carPhotoPath(photos)
              .color(car.getColor())
              .carNumber(car.getCarNumber())
              .autoModel(car.getAutoModel().getName())
              .build();
     }
}
