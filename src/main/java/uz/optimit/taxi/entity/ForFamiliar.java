package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.model.request.ForFamiliarRegisterRequestDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ForFamiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Pattern(regexp = "^[A-Za-z]*$")
    private String name;

    @NotBlank
    @Size(min = 9)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private short age;

    private double status;

    @ManyToOne
    private User user;

    public static ForFamiliar from(ForFamiliarRegisterRequestDto registerRequestDto, User user){
        return ForFamiliar.builder()
                .name(registerRequestDto.getName())
                .phone(registerRequestDto.getPhone())
                .gender(registerRequestDto.getGender())
                .age(registerRequestDto.getAge())
                .user(user)
                .build();
    }
}
