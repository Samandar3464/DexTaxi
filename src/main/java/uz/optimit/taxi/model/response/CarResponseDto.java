package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.Car;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponseDto {

     private UUID id;

     private String carNumber;

     private String color;

     private String texPassport;

     private String autoModel;

     private List<String> autoPhotosPath;

     private String texPassportPhotoPath;

     private UserResponseDto user;

     private boolean active;


     public static CarResponseDto from(Car car, String downloadUrl){
         Attachment texPassportPhoto1 = car.getTexPassportPhoto();
         String texPasswordPhotoLink = downloadUrl + texPassportPhoto1.getPath() + "/" + texPassportPhoto1.getNewName() + "." + texPassportPhoto1.getType();
         List<Attachment> autoPhotos1 = car.getAutoPhotos();
         List<String> carPhotoList = new ArrayList<>();
         autoPhotos1.forEach(attachment->
                 carPhotoList.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType()));

         return    CarResponseDto.builder()
                 .id(car.getId())
                 .carNumber(car.getCarNumber())
                 .color(car.getColor())
                 .texPassport(car.getTexPassport())
                 .autoModel(car.getAutoModel().getName())
                 .active(car.isActive())
                 .texPassportPhotoPath(texPasswordPhotoLink)
                 .autoPhotosPath(carPhotoList)
                 .build();
     }
}