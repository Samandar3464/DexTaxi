package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.model.response.AnnouncementDriverResponse;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementDriverService {

     private final AnnouncementDriverRepository repository;
     private final CarRepository carRepository;
     private final RegionRepository regionRepository;
     private final UserService userService;


     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
          User user = userService.checkUserExistByContext();
          AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcementDriverRegisterRequestDto, user, regionRepository,carRepository);
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
          Car car = carRepository.findByUserIdAndActive(driver.get().getUser().getId(), true);
          AnnouncementDriverResponse announcementDriverResponse = AnnouncementDriverResponse.from(driver.get(), car, AttachmentService.attachDownloadUrl);
          return new ApiResponse(announcementDriverResponse, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getDriverAnnouncements() {
          User user = userService.checkUserExistByContext();
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
