package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/add")
    public ApiResponse addCar(@ModelAttribute  @Validated  CarRegisterRequestDto carRegisterRequestDto) {
        return carService.addCar(carRegisterRequestDto);
    }

    @GetMapping("/getByUserId/{id}")
    public ApiResponse getByUserId(@PathVariable UUID id){
        return carService.getByUserId(id);
    }
}
