package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User  {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String surname;
    private String phone;
    private LocalDate birthDate;
    private String password;
    private LocalDateTime registeredDate;
    private boolean isBlocked;
    private double status;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToOne
    private Attachment profilePhoto;
    @ManyToMany
    private List<Role> role;

    @JsonIgnore
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Car> cars;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<AnnouncementUser> announcementUser;
    @JsonIgnore
    @ManyToMany
    private List<AnnouncementDriver> announcementDrivers;
}
