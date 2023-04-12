package uz.optimit.taxi.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;
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

@Service
public class AnnouncementDriverService {
     private final AnnouncementDriverRepository repository;
     private final CarRepository carRepository;
     private final RegionRepository regionRepository;
     private final UserRepository userRepository;

     public AnnouncementDriverService(AnnouncementDriverRepository repository, CarRepository carRepository, RegionRepository regionRepository, UserRepository userRepository) {
          this.repository = repository;
          this.carRepository = carRepository;
          this.regionRepository = regionRepository;
          this.userRepository = userRepository;
     }

     @ResponseStatus(HttpStatus.CREATED)
     public ResponseEntity<?> add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (!authentication.isAuthenticated() && authentication.getPrincipal().equals("anonymousUser")) {
               throw new UserNotFoundException("User not found");
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException("user not found"));
          AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcementDriverRegisterRequestDto, user, regionRepository);
          repository.save(announcementDriver);
          return new ResponseEntity<>("Successfully", HttpStatus.CREATED);
     }

     @ResponseStatus(HttpStatus.OK)
     public ResponseEntity<?> getDriverListForAnonymousUser() {
          List<AnnouncementDriver> all = repository.findAllByActive(true);
          List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
          all.forEach(announcementDriver -> {
               driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver));
          });
          return new ResponseEntity<>(driverResponses, HttpStatus.OK);
     }

     @ResponseStatus(HttpStatus.OK)
     public ResponseEntity<?> getDriverList() {

          List<AnnouncementDriverResponse> driverResponses = new ArrayList<>();
          List<AnnouncementDriver> driverList = repository.findAllByActive(true);
          List<Car> allByActive = carRepository.findAllByActive(true);

          for (AnnouncementDriver announcementDriver : driverList) {
               for (Car car : allByActive) {
                    if (announcementDriver.getUser().getId() == car.getUser().getId()) {
                         driverResponses.add(AnnouncementDriverResponse.from(announcementDriver, car, "C:/Users/99890/IdeaProjects/DexTaxi/src/main/resources/static/image"));
                    }
               }
          }
          return new ResponseEntity<>(driverResponses, HttpStatus.OK);
     }

}
