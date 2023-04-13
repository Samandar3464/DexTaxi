package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.exception.UserNotFoundException;
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
    public ApiResponse addCar(CarRegisterRequestDto carRegisterRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UserNotFoundException("User not found");
        }
        User principal = (User) authentication.getPrincipal();
        User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException("user not found"));
        Car car = Car.from(carRegisterRequestDto, autoModelRepository, attachmentService, user);
        carRepository.save(car);
        return new ApiResponse("Successfully ", true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse disActiveCarList() {
        List<Car> allByActive = carRepository.findAllByActive(false);
        List<CarResponseDto> carResponseDtoList = new ArrayList<>();
        allByActive.forEach(car -> carResponseDtoList.add(CarResponseDto.from(car, attachmentService.attachDownloadUrl)));
        return new ApiResponse(carResponseDtoList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getCarById(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(()->new RecordNotFoundException("car not found"));
        CarResponseDto carResponseDto = CarResponseDto.from(car, attachmentService.attachDownloadUrl);
        return new ApiResponse(carResponseDto, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse activateCar(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(()->new RecordNotFoundException("car not found"));
        car.setActive(true);
        carRepository.save(car);
        return new ApiResponse("Car activated", true);
    }

}
