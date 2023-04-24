package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementAlreadyExistException;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.CarNotFound;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.model.response.AnnouncementDriverResponse;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.SeatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     private final SeatRepository seatRepository;
     private final RegionRepository regionRepository;
     private final UserService userService;
     private final AttachmentService attachmentService;
     private final SeatService seatService;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
          User user = userService.checkUserExistByContext();
          if (user.getCars().isEmpty()) {
               throw new CarNotFound(CAR_NOT_FOUND);
          }
          Optional<AnnouncementDriver> byUserIdAndActive = repository.findByUserIdAndActive(user.getId(), true);
          if (byUserIdAndActive.isPresent()) {
               throw new AnnouncementAlreadyExistException(YOU_ALREADY_HAVE_ACTIVE_ANNOUNCEMENT);
          }
          Car car = carRepository.findByUserIdAndActive(user.getId(), true).orElseThrow(() -> new CarNotFound(CAR_NOT_FOUND));
          AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcementDriverRegisterRequestDto, user, regionRepository, car);
          seatService.onActive(announcementDriverRegisterRequestDto.getSeatIdList());
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
          AnnouncementDriver announcementDriver = repository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
          Car car = carRepository.findByUserIdAndActive(announcementDriver.getUser().getId(), true).orElseThrow(() ->
              new CarNotFound(CAR_NOT_FOUND));
          AnnouncementDriverResponse announcementDriverResponse = AnnouncementDriverResponse.from(announcementDriver, car, attachmentService.attachDownloadUrl);
          return new ApiResponse(announcementDriverResponse, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getDriverAnnouncements() {
          User user = userService.checkUserExistByContext();
          List<AnnouncementDriver> announcementDrivers = repository.findAllByUserIdAndActive(user.getId(), true);
          List<AnnouncementDriverResponse> announcementDriverResponses = new ArrayList<>();
          for (AnnouncementDriver announcementDriver : announcementDrivers) {
               announcementDriverResponses.add(AnnouncementDriverResponse.from(announcementDriver, announcementDriver.getCar(), attachmentService.attachDownloadUrl));
          }
          return new ApiResponse(announcementDriverResponses, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse deleteDriverAnnouncement(UUID id) {
          AnnouncementDriver announcementDriver = repository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
          announcementDriver.setActive(false);
          repository.save(announcementDriver);
          return new ApiResponse(DELETED, true);
     }

     public ApiResponse getHistory() {
          User user = userService.checkUserExistByContext();
          List<AnnouncementDriver> allByActive = repository.findAllByUserIdAndActive(user.getId(), false);
          List<AnnouncementDriverResponse> response = new ArrayList<>();
          allByActive.forEach((announcementDriver) -> response.add(AnnouncementDriverResponse.from(announcementDriver, announcementDriver.getCar(), attachmentService.attachUploadFolder)));
          return new ApiResponse(response, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getByFilter(Integer from, Integer to, String fromTime, String toTime) {
          List<AnnouncementDriver> all = getAnnouncementDrivers(from,to,fromTime,toTime);
          List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
          all.forEach(announcementDriver -> {
               driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver));
          });
          return new ApiResponse(driverResponses, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getByFilter(Integer from, Integer to, String fromTime, String toTime, int size) {
          List<AnnouncementDriver> all = getAnnouncementDrivers(from, to, fromTime, toTime);
          List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
          all.forEach(announcementDriver -> {
               List<Seat> allByCarIdAndActive = seatRepository.findAllByCarIdAndActive(announcementDriver.getCar().getId(), true);
               if (allByCarIdAndActive.size() >= size) {
                    driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver));
               }
          });
          return new ApiResponse(driverResponses, true);
     }

     private List<AnnouncementDriver> getAnnouncementDrivers(Integer from, Integer to, String fromTime, String toTime) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
          LocalDateTime fromTime1 = LocalDateTime.parse(fromTime, formatter);
          LocalDateTime toTime1 = LocalDateTime.parse(toTime, formatter);
          return repository.findAllByActiveAndFromRegionIdAndToRegionIdAndTimeToDriveBetweenOrderByCreatedTimeDesc(true, from, to, fromTime1, toTime1);
     }
}
