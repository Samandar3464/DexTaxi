package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Enum.Constants;
import uz.optimit.taxi.entity.LuggagePassenger;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.FamiliarNotFound;
import uz.optimit.taxi.exception.LuggageAnnouncementAlreadyExist;
import uz.optimit.taxi.exception.LuggageAnnouncementNotFound;
import uz.optimit.taxi.model.request.LuggagePassengerRequestDto;
import uz.optimit.taxi.model.response.LuggagePassengerResponse;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.LuggagePassengerRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LuggagePassengerService {

     private final LuggagePassengerRepository luggagePassengerRepository;
     private final FamiliarRepository familiarRepository;
     private final RegionRepository regionRepository;
     private final CityRepository cityRepository;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(LuggagePassengerRequestDto l) {

          Optional<LuggagePassenger> luggagePassenger = luggagePassengerRepository.findBySenderIdAndReceiverIdAndActive(l.getSenderId(), l.getReceiverId(), true);
          if (luggagePassenger.isPresent()) {
               throw  new LuggageAnnouncementAlreadyExist(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_ALREADY_EXIST);
          }
          if (familiarRepository.findById(l.getReceiverId()).isEmpty() || familiarRepository.findById(l.getSenderId()).isEmpty()) {
               throw new FamiliarNotFound(Constants.FAMILIAR_NOT_FOUND);
          }
          luggagePassengerRepository.save(LuggagePassenger.from(l, regionRepository, cityRepository, familiarRepository));
          return new ApiResponse(Constants.SUCCESSFULLY, true);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getById(UUID id) {
          LuggagePassenger byId = luggagePassengerRepository.findByIdAndActive(id, true)
              .orElseThrow(()->new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND));
          return new ApiResponse(LuggagePassengerResponse.from(byId), true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse ofActive(UUID id) {
          LuggagePassenger byId = luggagePassengerRepository.findByIdAndActive(id, true)
              .orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND));
          byId.setActive(false);
          luggagePassengerRepository.save(byId);
          return new ApiResponse(Constants.DELETED, true);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getList() {
          List<LuggagePassengerResponse> responseList = new ArrayList<>();
          luggagePassengerRepository.findAllByActive(true).forEach(luggagePassenger -> {
               responseList.add(LuggagePassengerResponse.from(luggagePassenger));
          });
          return new ApiResponse(responseList, true);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getByFilter(Integer fromRegion, Integer toRegion, Integer fromCity, Integer toCity, String timeToLeave) {
          List<LuggagePassengerResponse> responseList = new ArrayList<>();
          getLuggagePassengerList(fromRegion, toRegion, fromCity, toCity, timeToLeave).forEach(luggagePassenger -> {
               responseList.add(LuggagePassengerResponse.from(luggagePassenger));
          });
          return new ApiResponse(responseList, true);
     }

     private List<LuggagePassenger> getLuggagePassengerList(Integer fromRegion, Integer toRegion, Integer fromCity, Integer toCity, String timeToLeave) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
          LocalDateTime fromTime = LocalDateTime.parse(timeToLeave + " 00:01", formatter);
          LocalDateTime toTime = LocalDateTime.parse(timeToLeave + " 23:59", formatter);
          return luggagePassengerRepository.findAllByFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(fromRegion, toRegion, fromCity, toCity, fromTime, toTime);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getByFilter(Integer fromRegion, Integer toRegion, String timeToLeave) {
          List<LuggagePassengerResponse> responseList = new ArrayList<>();
          getLuggagePassengerList(fromRegion, toRegion, timeToLeave).forEach(luggagePassenger -> {
               responseList.add(LuggagePassengerResponse.from(luggagePassenger));
          });
          return new ApiResponse(responseList, true);
     }

     private List<LuggagePassenger> getLuggagePassengerList(Integer fromRegion, Integer toRegion, String timeToLeave) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
          LocalDateTime fromTime = LocalDateTime.parse(timeToLeave + " 00:01", formatter);
          LocalDateTime toTime = LocalDateTime.parse(timeToLeave + " 23:59", formatter);
          return luggagePassengerRepository.findAllByFromRegionIdAndToRegionIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(fromRegion, toRegion, fromTime, toTime);
     }
}
