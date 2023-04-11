package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Car  {

    @Id
    @GeneratedValue
    private UUID id;

    private String carNumber;
    private String color;

    private String carPassport;

    private String texPassport;

    @ManyToOne
    private AutoModel autoModel;

    @ManyToMany(mappedBy = "car")
    private List<Attachment> autoPhotos;

    @OneToOne
    private Attachment passportPhoto;

    @OneToOne
    private Attachment texPassportPhoto;

    @ManyToOne
    private User user;
}
