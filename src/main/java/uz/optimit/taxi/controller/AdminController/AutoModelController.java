
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;
import uz.optimit.taxi.service.AutoModelService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/autoModel")
public class AutoModelController {

    private final AutoModelService autoModelService;

    @PostMapping("/add")
    //    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addAutoModel(@RequestBody AutoModelRegisterRequestDto autoModelRegisterRequestDto) {
        return autoModelService.addAutoCategory(autoModelRegisterRequestDto);
    }
    @GetMapping("/getModelById/{id}")
    public ApiResponse getModelById(@PathVariable int id){
        return autoModelService.getModelById(id);
    }
    @GetMapping("/getModelList/")
    public ApiResponse getModelList(){
        return autoModelService.getModelList();
    }
}
