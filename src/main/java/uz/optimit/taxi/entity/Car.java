package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.model.request.SeatService;
import uz.optimit.taxi.repository.AutoModelRepository;
import uz.optimit.taxi.service.AttachmentService;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String carNumber;

    private String color;

    private String texPassport;

    @ManyToOne
    private AutoModel autoModel;

    @OneToMany
    private List<Attachment> autoPhotos;

    @OneToOne
    private Attachment texPassportPhoto;

    @OneToOne
    private Attachment photoDriverLicense;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "car" ,cascade = CascadeType.ALL)
    private List<Seat> seatList;

    private boolean active;

    public static Car from(CarRegisterRequestDto carRegisterRequestDto) {
        return Car.builder()
                .color(carRegisterRequestDto.getColor())
                .texPassport(carRegisterRequestDto.getTexPassport())
                .carNumber(carRegisterRequestDto.getCarNumber())
                .active(false)
                .build();
    }
}
