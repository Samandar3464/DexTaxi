package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "region")
    private List<City> cities;

    @OneToMany(mappedBy = "fromRegion")
    @JsonIgnore
    private List<LuggageDriver> luggageDriverList;

    @OneToMany(mappedBy = "toRegion")
    @JsonIgnore
    private List<LuggageDriver> luggageDrivers;

    @OneToMany(mappedBy = "fromRegion")
    @JsonIgnore
    private List<LuggagePassenger> luggagePassengers;

    @OneToMany(mappedBy = "toRegion")
    @JsonIgnore
    private List<LuggagePassenger> luggagePassengerList;

    @JsonIgnore
    @OneToMany(mappedBy = "fromRegion")
    private List<AnnouncementPassenger> fromAnnouncementUser;

    @JsonIgnore
    @OneToMany(mappedBy = "toRegion")
    private List<AnnouncementPassenger> toAnnouncementUser;

}
