package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CityRequestDto;
import uz.optimit.taxi.service.CityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/city")
public class CityController {
     private final CityService cityService;

     @PostMapping("/add")
     //    @PreAuthorize("hasRole('ADMIN')")
     public ApiResponse addRegion(@RequestBody CityRequestDto cityRequestDto) {
          return cityService.saveCity(cityRequestDto);
     }
}
