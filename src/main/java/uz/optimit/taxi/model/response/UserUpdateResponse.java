package uz.optimit.taxi.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.entity.User;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponse {

    private UUID id;
    private String fullName;
    private String phone;
    private String brithDay;
    private Gender gender;
    private String profilePhotoUrl;

    public static UserUpdateResponse fromDriver(User user, String downloadUrl) {
        String photoLink = null;
        if (user.getProfilePhoto() != null) {
            Attachment attachment = user.getProfilePhoto();
            photoLink = downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
        } else {
//            photoLink = downloadUrl + "avatar.png";
            photoLink = "https://sb.kaleidousercontent.com/67418/992x558/7632960ff9/people.png";
        }

        return UserUpdateResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .brithDay(user.getBirthDate().toString())
                .gender(user.getGender())
                .profilePhotoUrl(photoLink)
                .build();
    }
}
