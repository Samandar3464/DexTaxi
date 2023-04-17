package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.service.AutoCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/autoCategory")
public class AutoCategoryController {

    private final AutoCategoryService autoCategoryService;

    @PostMapping("/add")
    //    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addRegion(@RequestBody AutoCategoryRegisterRequestDto autoCategoryRegisterRequestDto){
        return autoCategoryService.addAutoCategory(autoCategoryRegisterRequestDto);
    }

    @GetMapping("/getCategoryById/{id}")
    public ApiResponse getCategoryById(@PathVariable int id){
        return autoCategoryService.getCategoryById(id);
    }
    @GetMapping("/getCategoryList/")
    public ApiResponse getModelList(){
        return autoCategoryService.getCategoryList();
    }
}
