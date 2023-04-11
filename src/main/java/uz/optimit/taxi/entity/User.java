package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.optimit.taxi.model.DriverRegisterDto;
import uz.optimit.taxi.service.AttachmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Pattern(regexp = "^[A-Za-z]*$")
    private String name;

    @Pattern(regexp = "^[A-Za-z]*$")
    private String surname;

    @NotBlank
    @Size(min = 9)
    private String phone;

    @NotBlank
    @Size(min = 6)
    private String password;

    private LocalDate birthDate;

    @Size(min = 9 , max = 9)
    private String passportNumber;
    @OneToOne
    private Attachment passportPhoto;

    private LocalDateTime registeredDate;

    private boolean isBlocked;

    private double status;

    private Integer verificationCode;

    private LocalDateTime verificationCodeLiveTime;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    private Attachment profilePhoto;

    @ManyToMany
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Car> cars;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<AnnouncementUser> announcementUser;

    @JsonIgnore
    @ManyToMany
    private List<AnnouncementDriver> announcementDrivers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public static User fromDriver(DriverRegisterDto driverRegisterDto, PasswordEncoder passwordEncoder, AttachmentService attachmentService , Integer verificationCode){
        return User.builder()
                .name(driverRegisterDto.getName())
                .surname(driverRegisterDto.getSurname())
                .phone(driverRegisterDto.getPhone())
                .birthDate(driverRegisterDto.getBirthDate())
                .gender(driverRegisterDto.getGender())
                .registeredDate(LocalDateTime.now())
                .verificationCode(verificationCode)
                .verificationCodeLiveTime(LocalDateTime.now())
                .password(passwordEncoder.encode(driverRegisterDto.getPassword()))
                .passportNumber(driverRegisterDto.getPassportNumber())
                .passportPhoto(attachmentService.saveToSystem(driverRegisterDto.getPassportPhoto()))
                .profilePhoto(attachmentService.saveToSystem(driverRegisterDto.getProfilePhoto()))

                .isBlocked(false)
                .build();
    }
}
