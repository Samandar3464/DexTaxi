package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.repository.AutoModelRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final AttachmentService attachmentService;
    private final AutoModelRepository autoModelRepository;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> addCar(CarRegisterRequestDto carRegisterRequestDto) {
        Car car = Car.from(carRegisterRequestDto, autoModelRepository, attachmentService, userRepository);
        carRepository.save(car);
        return new ResponseEntity<>("Successfully ", HttpStatus.OK);
    }
}
