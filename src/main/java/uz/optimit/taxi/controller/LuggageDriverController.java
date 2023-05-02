package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.LuggageDriverRequestDto;
import uz.optimit.taxi.model.request.LuggagePassengerRequestDto;
import uz.optimit.taxi.service.LuggageDriverService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/luggageDriver")
@RequiredArgsConstructor
public class LuggageDriverController {

     private final LuggageDriverService luggageDriverService;

     @PostMapping("/add")
     public ApiResponse add(@RequestBody LuggageDriverRequestDto luggageDriverRequestDto) {
          return luggageDriverService.add(luggageDriverRequestDto);
     }

     @GetMapping("/getList")
     public ApiResponse getList(){
          return luggageDriverService.getList();
     }

     @GetMapping("/getById/{id}")
     public ApiResponse getById(@PathVariable UUID id){
          return luggageDriverService.getById(id);
     }

     @GetMapping("/getListByFilter/{fromRegionId}/{toRegionId}/{fromCityId}/{toCityId}/{timeToLeave}")
     public ApiResponse getListByFilter
         (
             @PathVariable Integer fromRegionId,
             @PathVariable Integer toRegionId,
             @PathVariable Integer fromCityId,
             @PathVariable Integer toCityId,
             @PathVariable String timeToLeave
     ){
          return luggageDriverService.getByFilter(fromRegionId,toRegionId,fromCityId,toCityId,timeToLeave);
     }

     @GetMapping("/getListByFilter/{fromRegionId}/{toRegionId}/{timeToLeave}")
     public ApiResponse getByFilter
         (
             @PathVariable Integer fromRegionId,
             @PathVariable Integer toRegionId,
             @PathVariable String timeToLeave
     ){
          return luggageDriverService.getByFilter(fromRegionId,toRegionId,timeToLeave);
     }

     @DeleteMapping("/ofActive/{id}")
     public ApiResponse ofActive(@PathVariable UUID id){
          return luggageDriverService.ofActive(id);
     }

}
