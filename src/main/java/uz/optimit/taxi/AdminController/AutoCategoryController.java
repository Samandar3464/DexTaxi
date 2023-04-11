package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.service.AutoCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/autoCategory")
public class AutoCategoryController {
    private final AutoCategoryService autoCategoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addRegion(@RequestBody AutoCategoryRegisterRequestDto autoCategoryRegisterRequestDto){
        return autoCategoryService.addAutoCategory(autoCategoryRegisterRequestDto);
    }
}