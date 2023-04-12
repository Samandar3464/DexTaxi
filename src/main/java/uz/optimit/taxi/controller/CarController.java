package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.service.CarService;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/add")
    public ApiResponse addCar(@ModelAttribute  @Validated  CarRegisterRequestDto carRegisterRequestDto) {
        return carService.addCar(carRegisterRequestDto);
    }
}
