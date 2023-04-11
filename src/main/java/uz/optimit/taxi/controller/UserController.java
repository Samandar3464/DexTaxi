package uz.optimit.taxi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.model.request.PassengerRegisterDto;
import uz.optimit.taxi.model.request.UserLoginRequestDto;
import uz.optimit.taxi.model.request.DriverRegisterDto;
import uz.optimit.taxi.model.request.UserVerifyRequestDto;
import uz.optimit.taxi.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    @PostMapping("/register/driver")
    public ResponseEntity<?> registerDriver(@ModelAttribute @Validated DriverRegisterDto driverRegisterDto){
        return userService.registerDriver(driverRegisterDto);
    }
    @PostMapping("/register/passenger")
    public ResponseEntity<?> registerPassenger(@RequestBody PassengerRegisterDto passengerRegisterDto){
        return userService.registerPassenger(passengerRegisterDto);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto){
        return userService.login(userLoginRequestDto);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto){
        return userService.verify(userVerifyRequestDto);
    }
}
