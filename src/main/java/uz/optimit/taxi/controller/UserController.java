package uz.optimit.taxi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.*;
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
     public ApiResponse refreshToken(HttpServletRequest httpServletRequest) throws Exception {
          return userService.getToken(httpServletRequest);
     }

     @GetMapping("/getById/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getUserById(@PathVariable UUID id){
         return userService.getByUserId(id);
     }
     @GetMapping("/getByToken")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse checkUserResponseExistById(){
         return userService.checkUserResponseExistById();
     }

     @PostMapping("/setStatus")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse setStatus(@RequestBody StatusDto statusDto){
         return userService.setStatus(statusDto);
     }

     @PutMapping("/block/{id}")
     @PreAuthorize("hasAnyRole('ADMIN')")
     public ApiResponse blockUserById(@PathVariable UUID id) {
          return userService.deleteUserByID(id);
     }

     @PostMapping("/setFireBaseToken")
     public ApiResponse setFireBaseToken(@RequestBody FireBaseTokenRegisterDto fireBaseTokenRegisterDto){
          return userService.saveFireBaseToken(fireBaseTokenRegisterDto);
     }

     @PutMapping("/update")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse update(@ModelAttribute  UserUpdateDto userUpdateDto){
          return userService.updateUser(userUpdateDto);
     }

     @PostMapping("/changePassword")
     public ApiResponse changePassword(
         @RequestParam String number,
         @RequestParam String password
     ) {
          return userService.changePassword(number,password);
     }

     @PostMapping("/forgetPassword")
     public ApiResponse forgetPassword(@RequestBody String number) {
          return userService.forgetPassword(number);
     }


}
