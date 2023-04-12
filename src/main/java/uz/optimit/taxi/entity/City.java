package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

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

    @Column(unique = true)
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
