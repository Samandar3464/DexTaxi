package uz.optimit.taxi.model.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.optimit.taxi.entity.Enum.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

     private MultipartFile profilePhoto;

     private String fullName;

     private LocalDate brithDay;

     @Enumerated(EnumType.STRING)
     private Gender gender;
}