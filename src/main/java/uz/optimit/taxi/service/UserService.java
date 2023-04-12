package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.TokenResponse;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.exception.TimeExceededException;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.DriverRegisterDto;
import uz.optimit.taxi.model.request.PassengerRegisterDto;
import uz.optimit.taxi.model.request.UserLoginRequestDto;
import uz.optimit.taxi.model.request.UserVerifyRequestDto;
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

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerDriver(DriverRegisterDto driverRegisterDto) {
        Optional<User> byPhone = userRepository.findByPhone(driverRegisterDto.getPhone());
        if (byPhone.isPresent()) {
            throw new UserAlreadyExistException("Bu telefon raqam allaqachon ro'yhatdan o'tgan");
        }
        Integer verificationCode = verificationCodeGenerator();
        System.out.println("verificationCode = " + verificationCode);
        User user = User.fromDriver(driverRegisterDto, passwordEncoder, attachmentService, verificationCode);
        User save = userRepository.save(user);
        return new ResponseEntity<>("Successfully userId "+save.getId()+" verification code :"+verificationCode , HttpStatus.CREATED);
    }


    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerPassenger(PassengerRegisterDto passengerRegisterDto) {
        Optional<User> byPhone = userRepository.findByPhone(passengerRegisterDto.getPhone());
//        if (byPhone.isPresent()) {
//            throw new UserAlreadyExistException("Bu telefon raqam allaqachon ro'yhatdan o'tgan");
//        }
        Integer verificationCode = verificationCodeGenerator();
        System.out.println("verificationCode = " + verificationCode);
        User user = User.fromPassenger(passengerRegisterDto, passwordEncoder,attachmentService, verificationCode);
        userRepository.save(user);
        return new ResponseEntity<>("User added"+" verification code :"+verificationCode, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            TokenResponse tokenResponse = null;
            if (authenticate != null){
                String access = jwtService.generateAccessToken(userLoginRequestDto.getPhone());
                String refresh = jwtService.generateRefreshToken(userLoginRequestDto.getPhone());
                tokenResponse = new TokenResponse(access,refresh);
            }
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("User not found");
        }
    }



    private Integer verificationCodeGenerator() {
        return RandomGenerator.getDefault().nextInt(100000, 999999);
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> verify(UserVerifyRequestDto userVerifyRequestDto) {
        User user = userRepository.findByPhoneAndVerificationCode(userVerifyRequestDto.getPhone(), userVerifyRequestDto.getVerificationCode())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        if (!verificationCodeLiveTime(user.getVerificationCodeLiveTime())) {
//            throw new TimeExceededException("Verification code live time end");
//        }
        user.setVerificationCode(0);
        user.setBlocked(true);
        userRepository.save(user);
        return new ResponseEntity<>("User verified successfully ", HttpStatus.OK);
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

    public String getToken(String refreshToken) {
        return jwtService.getAccessTokenByRefresh(refreshToken);
    }
}


