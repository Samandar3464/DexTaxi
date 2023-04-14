package uz.optimit.taxi.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.model.response.AnnouncementDriverResponse;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
public class AnnouncementDriverService {

     private final AnnouncementDriverRepository repository;
     private final CarRepository carRepository;
     private final RegionRepository regionRepository;
     private final UserRepository userRepository;
     private final AttachmentService attachmentService;


     public AnnouncementDriverService(AnnouncementDriverRepository repository, CarRepository carRepository, RegionRepository regionRepository, UserRepository userRepository, AttachmentService attachmentService) {
          this.repository = repository;
          this.carRepository = carRepository;
          this.regionRepository = regionRepository;
          this.userRepository = userRepository;
          this.attachmentService = attachmentService;
     }

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
          AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcementDriverRegisterRequestDto, user, regionRepository);
          repository.save(announcementDriver);
          return new ApiResponse(SUCCESSFULLY, true);
     }


     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getDriverListForAnonymousUser() {
          List<AnnouncementDriver> all = repository.findAllByActive(true);
          List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
          all.forEach(announcementDriver -> {
               driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver));
          });
          return new ApiResponse(driverResponses, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getById(UUID id) {
          Optional<AnnouncementDriver> driver = repository.findById(id);
          Car car = carRepository.findByActiveAndUserId(true,driver.get().getUser().getId());
          AnnouncementDriverResponse announcementDriverResponse = AnnouncementDriverResponse.from(driver.get(), car, attachmentService.attachDownloadUrl);
          return new ApiResponse(announcementDriverResponse, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getDriverAnnouncements() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User user = (User) authentication.getPrincipal();
          List<AnnouncementDriver> announcementDrivers = repository.findAllByActiveAndUserId(true,user.getId());
          return new ApiResponse(announcementDrivers, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse deleteDriverAnnouncement(UUID id){
          AnnouncementDriver announcementDriver = repository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
          announcementDriver.setActive(false);
          repository.save(announcementDriver);
          return new ApiResponse(DELETED ,true);

     }
}
