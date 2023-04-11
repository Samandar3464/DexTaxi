package uz.optimit.taxi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.Jwt;
import uz.optimit.taxi.model.UserLoginRequestDto;
import uz.optimit.taxi.model.DriverRegisterDto;
import uz.optimit.taxi.model.UserVerifyRequestDto;
import uz.optimit.taxi.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

    private UserService userService;

    @PostMapping("user/register/driver")
    public ResponseEntity<?> registerDriver(@ModelAttribute @Validated DriverRegisterDto driverRegisterDto){
        return userService.register(driverRegisterDto);
    }

    @PostMapping("user/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto){
        return userService.login(userLoginRequestDto);
    }

    @PostMapping("user/verify")
    public ResponseEntity<?> verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto){
        return userService.verify(userVerifyRequestDto);
    }

    @PostMapping("user/refreshToken")
    public String verifyl(@RequestBody Jwt token){
        return userService.refreshToken(token.getRefreshToken());
    }

    @GetMapping("a")
    public String verifyl(){
        return "keldik";
    }

}
