package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;
import uz.optimit.taxi.service.AutoModelService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/autoModel")
public class AutoModelController {
    private final AutoModelService autoModelService;

    @PostMapping("/add")
    public ResponseEntity<?> addAutoModel(@RequestBody AutoModelRegisterRequestDto autoModelRegisterRequestDto){
        return autoModelService.addAutoCategory(autoModelRegisterRequestDto);
    }
}
