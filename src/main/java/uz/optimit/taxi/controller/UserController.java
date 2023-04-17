package uz.optimit.taxi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.TokenResponse;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.UserLoginRequestDto;
import uz.optimit.taxi.model.request.UserRegisterDto;
import uz.optimit.taxi.model.request.UserVerifyRequestDto;
import uz.optimit.taxi.service.UserService;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

     private UserService userService;
     @PostMapping("/register")
     public ApiResponse registerUser(@ModelAttribute UserRegisterDto userRegisterDto) {
          return userService.registerUser(userRegisterDto);
     }

     @PostMapping("/login")
     public ApiResponse login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto) {
          return userService.login(userLoginRequestDto);
     }

     @PostMapping("/verify")
     public ApiResponse verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto) {
          return userService.verify(userVerifyRequestDto);
     }

     @PostMapping("get/token/refreshToken")
     public ApiResponse refreshToken(@RequestBody TokenResponse token) {
          return userService.getToken(token.getRefreshToken());
     }

     @GetMapping("/getById/{id}")
     public ApiResponse getUserById(@PathVariable UUID id){
         return userService.getByUserId(id);
     }
}
