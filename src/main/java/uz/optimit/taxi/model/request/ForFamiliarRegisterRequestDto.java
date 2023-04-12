package uz.optimit.taxi.model.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Enum.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForFamiliarRegisterRequestDto {
     private String name;
     private String phone;
     @Enumerated(EnumType.STRING)
     private Gender gender;
     private short age;
}
