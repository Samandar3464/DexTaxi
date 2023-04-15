package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Familiar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;


    @Size(min = 9,max = 9)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private short age;

    private double status;

    @ManyToOne
    @JsonIgnore
    private User user;

    public static Familiar from(FamiliarRegisterRequestDto registerRequestDto, User user){
        return Familiar.builder()
                .name(registerRequestDto.getName())
                .phone(registerRequestDto.getPhone())
                .gender(registerRequestDto.getGender())
                .age(registerRequestDto.getAge())
                .user(user)
                .build();
    }


}
