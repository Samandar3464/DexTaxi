package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
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

    private boolean active;

    public static Car from(CarRegisterRequestDto carRegisterRequestDto, AutoModelRepository autoModelRepository, AttachmentService attachmentService, User user) {
        return Car.builder()
                .autoModel(autoModelRepository.getByIdAndAutoCategoryId(carRegisterRequestDto.getAutoModelId(), carRegisterRequestDto.getAutoCategoryId()))
                .color(carRegisterRequestDto.getColor())
                .texPassport(carRegisterRequestDto.getTexPassport())
                .carNumber(carRegisterRequestDto.getCarNumber())
                .photoDriverLicense(attachmentService.saveToSystem(carRegisterRequestDto.getPhotoDriverLicense()))
                .texPassportPhoto(attachmentService.saveToSystem(carRegisterRequestDto.getTexPassportPhoto()))
                .autoPhotos(attachmentService.saveToSystemListFile(carRegisterRequestDto.getAutoPhotos()))
                .user(user)
                .active(false)
                .build();
    }
}
