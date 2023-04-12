package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.Enum.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String name;
    private String surname;
    private String passportNumber;
    private String phone;
    private int age;
    private double status;
    private Gender gender;
    private String profilePhotoUrl;

    public static UserResponseDto from(User user,String downloadUrl) {
        Attachment attachment = user.getProfilePhoto();
        String photoLink = downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .passportNumber(user.getPassportNumber())
                .phone(user.getPhone())
                .age(LocalDate.now().getYear() - user.getBirthDate().getYear())
                .gender(user.getGender())
                .profilePhotoUrl(photoLink)
                .build();
    }
}
