package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

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


    @Size(min = 9, max = 9)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private short age;

    private double status;

    private boolean active;

    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<LuggageDriver> luggageDriverList;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<LuggageDriver> luggageDrivers;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<LuggagePassenger> luggagePassengers;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<LuggagePassenger> luggagePassengerList;


    public static Familiar from(FamiliarRegisterRequestDto registerRequestDto, User user) {
        return Familiar.builder()
                .name(registerRequestDto.getName())
                .phone(registerRequestDto.getPhone())
                .gender(registerRequestDto.getGender())
                .age(registerRequestDto.getAge())
                .active(true)
                .user(user)
                .build();
    }

    public static Familiar fromUser(User user) {
        return Familiar.builder()
                .id(user.getId())
                .name(user.getFullName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .active(true)
                .age((short) (LocalDateTime.now().getYear() - user.getBirthDate().getYear()))
                .user(user)
                .build();
    }

}
