package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AutoCategory;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.repository.AutoCategoryRepository;

import java.util.Optional;

import static uz.optimit.taxi.entity.Enum.Constants.AUTO_CATEGORY_ALREADY_EXIST;
import static uz.optimit.taxi.entity.Enum.Constants.SUCCESSFULLY;

@Service
@RequiredArgsConstructor
public class AutoCategoryService {

    private final AutoCategoryRepository autoCategoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse addAutoCategory(AutoCategoryRegisterRequestDto autoCategoryRegisterRequestDto) {
        Optional<AutoCategory> byName = autoCategoryRepository.findByName(autoCategoryRegisterRequestDto.getName());
        if (byName.isPresent()) {
            throw new RecordAlreadyExistException(AUTO_CATEGORY_ALREADY_EXIST);
        }
       AutoCategory autoCategory = AutoCategory.builder().name(autoCategoryRegisterRequestDto.getName()).build();
        autoCategoryRepository.save(autoCategory);
        return new  ApiResponse(SUCCESSFULLY , true);
    }
}
