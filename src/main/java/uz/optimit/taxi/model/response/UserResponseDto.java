package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
     private UUID id;
     private String name;
     private String surname;
     private String phone;
     private int age;
     private double status;
     private Gender gender;
     private String profilePhotoUrl;
     private List<Familiar> passengersList;

     public static UserResponseDto from(User user, String downloadUrl) {
          String photoLink =null;
          if (user.getProfilePhoto()!=null){
               Attachment attachment = user.getProfilePhoto();
               photoLink = downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
          }else {
               photoLink=downloadUrl+"avatar.png";
          }

          return UserResponseDto.builder()
              .id(user.getId())
              .name(user.getName())
              .passengersList(user.getPassengersList())
              .surname(user.getSurname())
              .phone(user.getPhone())
              .age(LocalDate.now().getYear() - user.getBirthDate().getYear())
              .gender(user.getGender())
              .profilePhotoUrl(photoLink)
              .build();
     }
}
