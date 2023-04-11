package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AutoCategory;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.repository.AutoCategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutoCategoryService {

    private final AutoCategoryRepository autoCategoryRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addAutoCategory(AutoCategoryRegisterRequestDto autoCategoryRegisterRequestDto) {
        Optional<AutoCategory> byName = autoCategoryRepository.findByName(autoCategoryRegisterRequestDto.getName());
        if (byName.isPresent()) {
            throw new RecordAlreadyExistException("Auto category  already exist");
        }
       AutoCategory autoCategory = AutoCategory.builder().name(autoCategoryRegisterRequestDto.getName()).build();
        autoCategoryRepository.save(autoCategory);
        return new ResponseEntity<>("Successfully" , HttpStatus.CREATED);
    }
}
