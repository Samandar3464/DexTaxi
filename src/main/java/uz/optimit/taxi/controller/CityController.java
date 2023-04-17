package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CityRequestDto;
import uz.optimit.taxi.service.CityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/city")
public class CityController {
     private final CityService cityService;

     @PostMapping("/add")
     //    @PreAuthorize("hasRole('ADMIN')")
     public ApiResponse addRegion(@RequestBody CityRequestDto cityRequestDto) {
          return cityService.saveCity(cityRequestDto);
     }

     @GetMapping("/getList")
     public ApiResponse getCityList(){
          return cityService.getCityList();
     }

     @GetMapping("/getCityById/{id}")
     public ApiResponse getCityById(@PathVariable Integer id){
          return cityService.getCityById(id);
     }
}
