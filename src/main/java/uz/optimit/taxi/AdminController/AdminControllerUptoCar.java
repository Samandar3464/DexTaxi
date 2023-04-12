package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/car")
@RequiredArgsConstructor
public class AdminControllerUptoCar {

    private final CarService carService;

    @GetMapping("/dicActiveCars")
    public ResponseEntity<?> disActiveCarsList(){
        return carService.disActiveCarList();
    }


    @GetMapping("/getCar/{id}")
    public ResponseEntity<?> getCarById(@PathVariable("id") UUID id){
        return carService.getCarById(id);
    }

    @GetMapping("/activateCar/{id}")
    public ResponseEntity<?> activateCar(@PathVariable("id") UUID id){
        return carService.activateCar(id);
    }
}
