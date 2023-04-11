package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AutoCategory;
import uz.optimit.taxi.entity.AutoModel;
import uz.optimit.taxi.entity.Region;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;
import uz.optimit.taxi.repository.AutoCategoryRepository;
import uz.optimit.taxi.repository.AutoModelRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutoModerService {

    private final AutoModelRepository autoModelRepository;
    private final AutoCategoryRepository autoCategoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addAutoCategory(AutoModelRegisterRequestDto autoModelRegisterRequestDto) {
        Optional<AutoModel> byName = autoModelRepository.findByNameAndAutoCategoryId(autoModelRegisterRequestDto.getName(),autoModelRegisterRequestDto.getCategoryId());
        if (byName.isPresent()) {
            throw new RecordAlreadyExistException("Auto model  already exist");
        }
        AutoModel autoModel = AutoModel.builder()
                .name(autoModelRegisterRequestDto.getName())
                .autoCategory(autoCategoryRepository.getById(autoModelRegisterRequestDto.getCategoryId())).build();
        autoModelRepository.save(autoModel);
        return new ResponseEntity<>("Successfully" , HttpStatus.CREATED);
    }
}
