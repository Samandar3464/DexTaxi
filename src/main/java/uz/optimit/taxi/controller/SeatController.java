package uz.optimit.taxi.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.service.SeatService;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/seat/")
@RequiredArgsConstructor
public class SeatController {
     private final SeatService seatService;

     @PostMapping("onActive")
     public ApiResponse onActive(@RequestBody List<UUID> uuidList){
        return  seatService.onActive(uuidList);
     }

     @PostMapping("ofActive")
     public ApiResponse ofActive(@RequestBody List<UUID> uuidList){
          return seatService.ofActive(uuidList);
     }

     @GetMapping("getSeatListByCarId/{id}")
     public ApiResponse getSeatListByCarId(@PathVariable UUID id){
          return seatService.getSeatListByCarId(id);
     }

}
