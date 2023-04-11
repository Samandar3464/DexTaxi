package uz.optimit.taxi.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarRegisterRequestDto {


    private UUID userId;
    private UUID autoCategoryId;

    private UUID autoModelId;

    private String color;

    private String carNumber;

    private String texPassport;

    private List<MultipartFile> autoPhotos;

    private MultipartFile texPassportPhoto;

}
