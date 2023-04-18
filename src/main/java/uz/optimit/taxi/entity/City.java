package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @JsonIgnore
    @ManyToOne
    private Region region;

    @JsonIgnore
    @OneToMany(mappedBy = "fromCity")
    private List<AnnouncementPassenger> fromAnnouncementUser;

    @JsonIgnore
    @OneToMany(mappedBy = "toCity")
    private List<AnnouncementPassenger> toAnnouncementUser;

}
