package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.TokenResponse;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.UserLoginRequestDto;
import uz.optimit.taxi.model.request.UserRegisterDto;
import uz.optimit.taxi.model.request.UserVerifyRequestDto;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RoleRepository;
import uz.optimit.taxi.repository.UserRepository;
import uz.optimit.taxi.utils.JwtService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final FamiliarRepository familiarRepository;


    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse registerUser(UserRegisterDto userRegisterDto) {
        Optional<User> byPhone = userRepository.findByPhone(userRegisterDto.getPhone());
        if (byPhone.isPresent()) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        Integer verificationCode = verificationCodeGenerator();
        System.out.println("verificationCode = " + verificationCode);
        User user = User.fromPassenger(userRegisterDto, passwordEncoder, attachmentService, verificationCode, roleRepository);
        User save = userRepository.save(user);
        familiarRepository.save(Familiar.fromUser(save));
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(save.getPhone());
        return new ApiResponse(SUCCESSFULLY + " verification code :" + verificationCode, true,new TokenResponse(access, refresh));
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
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }




    @ResponseStatus(HttpStatus.OK)
    public ApiResponse verify(UserVerifyRequestDto userVerifyRequestDto) {
        User user = userRepository.findByPhoneAndVerificationCode(userVerifyRequestDto.getPhone(), userVerifyRequestDto.getVerificationCode())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
//        if (!verificationCodeLiveTime(user.getVerificationCodeLiveTime())) {
//            throw new TimeExceededException("Verification code live time end");
//        }
        user.setVerificationCode(0);
        user.setBlocked(true);
        userRepository.save(user);
        return new ApiResponse(USER_VERIFIED_SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getToken(String refreshToken) {
        String accessTokenByRefresh = jwtService.getAccessTokenByRefresh(refreshToken);
        return new ApiResponse(new TokenResponse(accessTokenByRefresh), true);
    }

    public  User checkUserExistByContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findByPhone(user.getPhone()).orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
    }


    public  User checkUserExistById(UUID id){
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
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

    public ApiResponse getByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new ApiResponse(UserResponseDto.from(user,attachmentService.attachDownloadUrl),true);
    }
}


