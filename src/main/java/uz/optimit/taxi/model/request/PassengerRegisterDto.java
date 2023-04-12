package uz.optimit.taxi.model.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class PassengerRegisterDto {

    @Pattern(regexp = "^[A-Za-z]*$")
    private String name;

    @Pattern(regexp = "^[A-Za-z]*$")
    private String surname;

    @NotBlank
    @Size(min = 9, max = 9)
    private String phone;
    private LocalDate birthDate;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private MultipartFile multipartFile;

}
