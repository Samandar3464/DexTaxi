package uz.optimit.taxi.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.configuration.jwtConfig.JwtGenerate;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.*;
import uz.optimit.taxi.model.response.*;
import uz.optimit.taxi.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.random.RandomGenerator;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final JwtGenerate jwtGenerate;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final FamiliarRepository familiarRepository;
    private final StatusRepository statusRepository;
    private final SmsService service;
    private final CountMassageRepository countMassageRepository;
    private final FireBaseMessagingService fireBaseMessagingService;

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByPhone(userRegisterDto.getPhone())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        Integer verificationCode = verificationCodeGenerator();
//        service.sendSms(SmsModel.builder()
//                .mobile_phone(userRegisterDto.getPhone())
//                .message("DexTaxi. Tasdiqlash kodi: " + verificationCode + " . Yo'linggiz bexatar bo'lsin.")
//                .from(4546)
//                .callback_url("http://0000.uz/test.php")
//                .build());
        countMassageRepository.save(new CountMassage(userRegisterDto.getPhone(), 1, LocalDateTime.now()));
        System.out.println("verificationCode = " + verificationCode);
        Status status = statusRepository.save(new Status(0, 0));
        User user = User.fromPassenger(userRegisterDto, passwordEncoder, attachmentService, verificationCode, roleRepository, status);
        User save = userRepository.save(user);
        familiarRepository.save(Familiar.fromUser(save));
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            User user = (User) authenticate.getPrincipal();
            String access = jwtGenerate.generateAccessToken(user);
            String refresh = jwtGenerate.generateRefreshToken(user);
            return new ApiResponse(new TokenResponse(access, refresh, UserResponseDto.fromDriver(user, attachmentService.attachDownloadUrl)), true);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse verify(UserVerifyRequestDto userVerifyRequestDto) {
        User user = userRepository.findByPhoneAndVerificationCode(userVerifyRequestDto.getPhone(), userVerifyRequestDto.getVerificationCode())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
//        if (!verificationCodeLiveTime(user.getVerificationCodeLiveTime())) {
//            throw new TimeExceededException(CODE_TIME_OUT);
//        }
        user.setVerificationCode(0);
        user.setBlocked(true);
        userRepository.save(user);
        String access = jwtGenerate.generateAccessToken(user);
        String refresh = jwtGenerate.generateRefreshToken(user);
        return new ApiResponse(USER_VERIFIED_SUCCESSFULLY, true, new TokenResponse(access, refresh, UserResponseDto.fromDriver(user, attachmentService.attachDownloadUrl)));
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getToken(HttpServletRequest request) throws Exception {
        String accessTokenByRefresh = jwtGenerate.checkRefreshTokenValidAndGetAccessToken(request);
        return new ApiResponse("NEW ACCESS TOKEN ", true, new TokenResponse(accessTokenByRefresh));
    }

    @ResponseStatus(HttpStatus.OK)
    public User checkUserExistByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findByPhone(user.getPhone()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse checkUserResponseExistById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findByPhone(user.getPhone()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new ApiResponse(UserUpdateResponse.fromDriver(user1, attachmentService.attachDownloadUrl), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public User checkUserExistById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.OK)
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

    @ResponseStatus(HttpStatus.OK)
    private Integer verificationCodeGenerator() {
        return RandomGenerator.getDefault().nextInt(100000, 999999);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new ApiResponse(UserResponseDto.fromDriver(user, attachmentService.attachDownloadUrl), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse setStatus(StatusDto statusDto) {
        User user = userRepository.findById(statusDto.getUserId()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
//        Optional<Status> statusOld = statusRepository.findByUserId(user.getId());
//        Status status = Status.fromForDriver(statusDto,statusOld.get());
        Status status = Status.from(statusDto, user.getStatus());
        Status save = statusRepository.save(status);
        user.setStatus(save);
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse addBlockUserByID(UUID id) {
        User user = checkUserExistById(id);
        Optional<User> byId = userRepository.findById(id);
        byId.get().setBlocked(false);
        userRepository.save(byId.get());
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.from(user.getFireBaseToken(), BLOCKED, new HashMap<>());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse openToBlockUserByID(UUID id) {
        User user = checkUserExistById(id);
        Optional<User> byId = userRepository.findById(id);
        byId.get().setBlocked(true);
        userRepository.save(byId.get());
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.from(user.getFireBaseToken(), OPEN, new HashMap<>());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse saveFireBaseToken(FireBaseTokenRegisterDto fireBaseTokenRegisterDto) {
        User user = userRepository.findById(fireBaseTokenRegisterDto.getUserId()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        user.setFireBaseToken(fireBaseTokenRegisterDto.getFireBaseToken());
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    public User addRoleDriver(List<Car> carList) {
        User user = userRepository.findByCarsIn(carList).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        List<Role> roles = user.getRoles();
        Role byName = roleRepository.findByName(DRIVER);
        if (!roles.contains(byName)) {
            roles.add((roleRepository.findByName(DRIVER)));
        }
        userRepository.save(user);
        return user;
    }

    public void deleteRoleDriver(List<Car> carList) {
        User user = userRepository.findByCarsIn(carList).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        List<Role> roles = user.getRoles();
        Role byName = roleRepository.findByName(DRIVER);
        if (roles.contains(byName)) {
            roles.remove(byName);
        }
        userRepository.save(user);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updateUser(UserUpdateDto userUpdateDto) {
        User user = checkUserExistByContext();
        user.setFullName(userUpdateDto.getFullName());
        user.setGender(userUpdateDto.getGender());
        if (userUpdateDto.getProfilePhoto() != null) {
            Attachment attachment = attachmentService.saveToSystem(userUpdateDto.getProfilePhoto());
            if (user.getProfilePhoto() != null) {
                attachmentService.deleteNewNameId(user.getProfilePhoto().getNewName() + "." + user.getProfilePhoto().getType());
            }
            user.setProfilePhoto(attachment);
        }
        user.setBirthDate(userUpdateDto.getBrithDay());
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse forgetPassword(String number) {
        User user =userRepository.findByPhone(number).orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
        Integer verificationCode = verificationCodeGenerator();
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
        System.out.println("Verification code: " + verificationCode);
        service.sendSms(SmsModel.builder()
                .mobile_phone(user.getPhone())
                .message("DexTaxi. Tasdiqlash kodi: " + verificationCode + "Yo'lingiz bexatar  bo'lsin")
                .from(4546)
                .callback_url("http://0000.uz/test.php")
                .build());
        countMassageRepository.save(new CountMassage(user.getPhone(), 1, LocalDateTime.now()));
        return new ApiResponse(SUCCESSFULLY,true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse changePassword(String number, String password) {
        User user = userRepository.findByPhone(number).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY ,true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse changePasswordFromProfile(String oldPassword, String newPassword) {
        User user = checkUserExistByContext();
        if (!passwordEncoder.matches(oldPassword,user.getPassword())){
          throw new RecordNotFoundException(OLD_PASSWORD_WRONG);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY ,true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse reSendSms(String number) {
        Integer integer = verificationCodeGenerator();
        System.out.println(integer);
        service.sendSms(SmsModel.builder()
                .mobile_phone(number)
                .message("DexTaxi. Tasdiqlash kodi: " + integer + " . Yo'linggiz bexatar bo'lsin.")
                .from(4546)
                .callback_url("http://0000.uz/test.php")
                .build());
        countMassageRepository.save(new CountMassage(number, 1, LocalDateTime.now()));
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse removeUserFromContext() {
        User user = checkUserExistByContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName().equals(user.getPhone())) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> all = userRepository.findAll(pageable);
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        all.forEach(obj -> userResponseDtoList.add(UserResponseDto.fromDriver(obj, attachmentService.attachDownloadUrl)));
        return new ApiResponse(new UserResponseListForAdmin(userResponseDtoList, all.getTotalElements(), all.getTotalPages(), all.getNumber()), true);
    }

    public User getUserByCar(Car car) {
        return userRepository.findByCarsIn(List.of(car)).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    public LocalDateTime getTime() {
        LocalDate now = LocalDate.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
    }
}


