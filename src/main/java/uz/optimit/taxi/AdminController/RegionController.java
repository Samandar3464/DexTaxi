package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.service.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/region")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/add")
    //    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addRegion(@RequestBody RegionRegisterRequestDto regionRegisterRequestDto){
       return regionService.addRegion(regionRegisterRequestDto);
    }
}
