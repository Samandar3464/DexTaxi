package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.model.response.CarResponseDto;
import uz.optimit.taxi.repository.AutoModelRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> disActiveCarList() {
        List<Car> allByActive = carRepository.findAllByActive(false);
        List<CarResponseDto> carResponseDtoList = new ArrayList<>();
        allByActive.forEach(car -> carResponseDtoList.add(CarResponseDto.from(car, attachmentService.attachDownloadUrl)));
        return new ResponseEntity<>(carResponseDtoList, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCarById(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(()->new RecordNotFoundException("car not found"));
        CarResponseDto carResponseDto = CarResponseDto.from(car, attachmentService.attachDownloadUrl);
        return new ResponseEntity<>(carResponseDto, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> activateCar(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(()->new RecordNotFoundException("car not found"));
        car.setActive(true);
        carRepository.save(car);
        return new ResponseEntity<>("Car activated", HttpStatus.OK);
    }

}
