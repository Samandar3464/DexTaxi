package uz.optimit.taxi.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.TokenResponse;
import uz.optimit.taxi.entity.api.ApiResponse;
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
    public ApiResponse registerDriver(@ModelAttribute @Validated DriverRegisterDto driverRegisterDto){
        return userService.registerDriver(driverRegisterDto);
    }
    @PostMapping("/register/passenger")
    public ApiResponse registerPassenger(@ModelAttribute PassengerRegisterDto passengerRegisterDto){
        return userService.registerPassenger(passengerRegisterDto);
    }
    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto){
        return userService.login(userLoginRequestDto);
    }
    @PostMapping("/verify")
    public ApiResponse verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto){
        return userService.verify(userVerifyRequestDto);
    }

    @PostMapping("get/token/refreshToken")
    public String refreshToken(@RequestBody TokenResponse token){
        return userService.getToken(token.getRefreshToken());
    }

}
