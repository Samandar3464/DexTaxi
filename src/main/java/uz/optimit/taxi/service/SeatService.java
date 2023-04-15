package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.repository.SeatRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
     private final SeatRepository seatRepository;
     private final UserRepository userRepository;

    public List<Seat> createCarSeats(byte count , Car car){
          List<Seat> seatList  = new ArrayList<>();
          for (byte i = 1; i <= count; i++) {
               seatList.add(seatRepository.save(
                   Seat.builder()
                       .seatNumber(i)
                       .active(false)
                       .car(car)
                       .build()));
          }
          return seatList;
     }

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse onActive(List<UUID> seatIdList) {
          List<Seat> byIdIn = seatRepository.findByIdIn(seatIdList);
          byIdIn.forEach(seat -> {
               seat.setActive(true);
          });
          seatRepository.saveAll(byIdIn);
          return new ApiResponse(byIdIn,true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse ofActive(List<UUID> seatIdList) {
          List<Seat> byIdIn = seatRepository.findByIdIn(seatIdList);
          byIdIn.forEach(seat -> {
               seat.setActive(false);
          });
          seatRepository.saveAll(byIdIn);
          return new ApiResponse(byIdIn,true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getSeatListByCarId(UUID carID){
          List<Seat> allByCarId = seatRepository.findAllByCar_Id(carID);
          return new ApiResponse(allByCarId,true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getSeatListByActive(UUID carID){
          List<Seat> allByCarId = seatRepository.findAllByCar_IdAndActive(carID,true);
          return new ApiResponse(allByCarId,true);
     }
}
