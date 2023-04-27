package uz.optimit.taxi.service;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Enum.Constants;
import uz.optimit.taxi.entity.LuggageDriver;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.LuggageAnnouncementAlreadyExist;
import uz.optimit.taxi.exception.LuggageAnnouncementNotFound;
import uz.optimit.taxi.model.request.LuggageDriverRequestDto;
import uz.optimit.taxi.model.response.LuggageDriverResponse;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.LuggageDriverRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LuggageDriverService {

     private final LuggageDriverRepository luggageDriverRepository;
     private final AttachmentService attachmentService;
     private final RegionRepository regionRepository;
     private final UserService userService;
     private final CityRepository cityRepository;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(LuggageDriverRequestDto luggageDriverRequestDto) {
          User user = userService.checkUserExistByContext();
          if (luggageDriverRepository.findBySupplierIdAndActive(user.getId(), true).isPresent()) {
              throw new LuggageAnnouncementAlreadyExist(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_ALREADY_EXIST);
          }
          luggageDriverRepository.save(LuggageDriver.from(luggageDriverRequestDto, regionRepository, cityRepository,user));
          return new ApiResponse(Constants.SUCCESSFULLY, true);
     }

     @JsonManagedReference
     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getList() {
          List<LuggageDriverResponse> responseList = new ArrayList<>();
          List<LuggageDriver> allByActive = luggageDriverRepository.findAllByActive(true);
          allByActive.forEach(luggageDriver -> {
                   responseList.add(LuggageDriverResponse.from(luggageDriver,attachmentService.attachDownloadUrl));
              });
          return new ApiResponse(responseList, true);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getById(UUID id) {
          LuggageDriver byId = luggageDriverRepository.findByIdAndActive(id, true)
              .orElseThrow(()-> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND));
          return new ApiResponse(LuggageDriverResponse.from(byId,attachmentService.attachDownloadUrl), true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse ofActive(UUID id) {
          LuggageDriver byId = luggageDriverRepository.findByIdAndActive(id, true)
              .orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND));
          byId.setActive(false);
          luggageDriverRepository.save(byId);
          return new ApiResponse(Constants.DELETED, true);
     }


     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getByFilter(Integer fromRegion, Integer toRegion, Integer fromCity, Integer toCity, String timeToLeave) {
          List<LuggageDriverResponse> responseList = new ArrayList<>();
          getLuggageDriverList(fromRegion, toRegion, fromCity, toCity, timeToLeave).forEach(luggageDriver -> {
               responseList.add(LuggageDriverResponse.from(luggageDriver,attachmentService.attachDownloadUrl));
          });
          return new ApiResponse(responseList, true);
     }

     private List<LuggageDriver> getLuggageDriverList(Integer fromRegion, Integer toRegion, Integer fromCity, Integer toCity, String timeToLeave) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
          LocalDateTime fromTime = LocalDateTime.parse(timeToLeave + " 00:01", formatter);
          LocalDateTime toTime = LocalDateTime.parse(timeToLeave + " 23:59", formatter);
          return luggageDriverRepository.findAllByFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(fromRegion, toRegion, fromCity, toCity, fromTime, toTime);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getByFilter(Integer fromRegion, Integer toRegion, String timeToLeave) {
          List<LuggageDriverResponse> responseList = new ArrayList<>();
          getLuggageDriverList(fromRegion, toRegion, timeToLeave).forEach(luggageDriver -> {
               responseList.add(LuggageDriverResponse.from(luggageDriver,attachmentService.attachDownloadUrl));
          });
          return new ApiResponse(responseList, true);
     }

     private List<LuggageDriver> getLuggageDriverList(Integer fromRegion, Integer toRegion, String timeToLeave) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
          LocalDateTime fromTime = LocalDateTime.parse(timeToLeave + " 00:01", formatter);
          LocalDateTime toTime = LocalDateTime.parse(timeToLeave + " 23:59", formatter);
          return luggageDriverRepository.findAllByFromRegionIdAndToRegionIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(fromRegion, toRegion, fromTime, toTime);
     }
}
