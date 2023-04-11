package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "fromRegion")
    private List<AnnouncementPassenger> fromAnnouncementUser;
    @JsonIgnore
    @OneToMany(mappedBy = "toRegion")
    private List<AnnouncementPassenger> toAnnouncementUser;

}
