package uz.optimit.taxi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.model.PassengerRegisterDto;
import uz.optimit.taxi.model.UserLoginRequestDto;
import uz.optimit.taxi.model.DriverRegisterDto;
import uz.optimit.taxi.model.UserVerifyRequestDto;
import uz.optimit.taxi.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    @PostMapping("/register/driver")
    public ResponseEntity<?> registerDriver(@ModelAttribute @Validated DriverRegisterDto driverRegisterDto){
        return userService.register(driverRegisterDto);
    }
//    @PostMapping("/register/passenger")
//    public ResponseEntity<?> registerPassenger(@ModelAttribute @Validated PassengerRegisterDto driverRegisterDto){
//        return userService.register(driverRegisterDto);
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto){
        return userService.login(userLoginRequestDto);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto){
        return userService.verify(userVerifyRequestDto);
    }
}
