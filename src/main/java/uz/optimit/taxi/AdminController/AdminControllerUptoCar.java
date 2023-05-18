package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.model.request.DenyCar;
import uz.optimit.taxi.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/car")
@RequiredArgsConstructor
public class AdminControllerUptoCar {

    private final CarService carService;

    @GetMapping("/dicActiveCars")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse disActiveCarsList(@RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "5") int size) {
        return carService.disActiveCarList(page, size);
    }

    @GetMapping("/getCar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getCarById(@PathVariable("id") UUID id) {
        return carService.getCarById(id);
    }

    @GetMapping("/activateCar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse activateCar(@PathVariable("id") UUID id) {
        return carService.activateCar(id);
    }

    @GetMapping("/deactivateCar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deactivateCar(@PathVariable("id") UUID id) {
        return carService.deactivateCar(id);
    }

    @PostMapping("/denyCar")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse denyCar(@RequestBody DenyCar denyCar) {
        return carService.denyCar(denyCar);
    }


}
