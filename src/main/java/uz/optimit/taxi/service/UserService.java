package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.TokenResponse;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.UserLoginRequestDto;
import uz.optimit.taxi.model.request.UserRegisterDto;
import uz.optimit.taxi.model.request.UserVerifyRequestDto;
import uz.optimit.taxi.repository.RoleRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;


    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse registerUser(UserRegisterDto userRegisterDto) {
        Optional<User> byPhone = userRepository.findByPhone(userRegisterDto.getPhone());
//        if (byPhone.isPresent()) {
//            throw new UserAlreadyExistException("Bu telefon raqam allaqachon ro'yhatdan o'tgan");
//        }
        Integer verificationCode = verificationCodeGenerator();
        System.out.println("verificationCode = " + verificationCode);
        User user = User.fromPassenger(userRegisterDto, passwordEncoder, attachmentService, verificationCode, roleRepository);
        userRepository.save(user);
        return new ApiResponse("User added" + " verification code :" + verificationCode, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            User user = (User) authenticate.getPrincipal();
            String access = jwtService.generateAccessToken(user);
            String refresh = jwtService.generateRefreshToken(userLoginRequestDto.getPhone());
            return new ApiResponse(new TokenResponse(access, refresh), true);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("User not found");
        }
    }




    @ResponseStatus(HttpStatus.OK)
    public ApiResponse verify(UserVerifyRequestDto userVerifyRequestDto) {
        User user = userRepository.findByPhoneAndVerificationCode(userVerifyRequestDto.getPhone(), userVerifyRequestDto.getVerificationCode())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        if (!verificationCodeLiveTime(user.getVerificationCodeLiveTime())) {
//            throw new TimeExceededException("Verification code live time end");
//        }
        user.setVerificationCode(0);
        user.setBlocked(true);
        userRepository.save(user);
        return new ApiResponse("User verified successfully ", true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getToken(String refreshToken) {
        String accessTokenByRefresh = jwtService.getAccessTokenByRefresh(refreshToken);
        return new ApiResponse(new TokenResponse(accessTokenByRefresh), true);
    }
    private boolean verificationCodeLiveTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth() - localDateTime.getDayOfMonth();
        int hour = now.getHour() - localDateTime.getHour();
        int minute = now.getMinute() - localDateTime.getMinute();
        if (day == 0 && hour == 0 && minute <= 2) {
            return true;
        }
        return false;
    }

    private Integer verificationCodeGenerator() {
        return RandomGenerator.getDefault().nextInt(100000, 999999);
    }
}


