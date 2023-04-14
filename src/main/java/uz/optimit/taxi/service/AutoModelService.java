package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AutoModel;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;
import uz.optimit.taxi.repository.AutoCategoryRepository;
import uz.optimit.taxi.repository.AutoModelRepository;

import java.util.Optional;

import static uz.optimit.taxi.entity.Enum.Constants.AUTO_MODEL_ALREADY_EXIST;
import static uz.optimit.taxi.entity.Enum.Constants.SUCCESSFULLY;

@Service
@RequiredArgsConstructor
public class AutoModelService {

    private final AutoModelRepository autoModelRepository;
    private final AutoCategoryRepository autoCategoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse addAutoCategory(AutoModelRegisterRequestDto autoModelRegisterRequestDto) {
        Optional<AutoModel> byName = autoModelRepository.findByNameAndAutoCategoryId(autoModelRegisterRequestDto.getName(), autoModelRegisterRequestDto.getCategoryId());
        if (byName.isPresent()) {
            throw new RecordAlreadyExistException(AUTO_MODEL_ALREADY_EXIST);
        }
        AutoModel autoModel = AutoModel.builder()
                .name(autoModelRegisterRequestDto.getName())
                .countSeat(autoModelRegisterRequestDto.getCountSeat())
                .autoCategory(autoCategoryRepository.getById(autoModelRegisterRequestDto.getCategoryId())).build();
        autoModelRepository.save(autoModel);
        return new ApiResponse(SUCCESSFULLY, true);
    }
}
