package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.LuggagePassengerRequestDto;
import uz.optimit.taxi.service.LuggagePassengerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/luggagePassenger")
@RequiredArgsConstructor
public class LuggagePassengerController {

     private final LuggagePassengerService luggagePassengerService;
     @PostMapping("/add")
     public ApiResponse add(@RequestBody LuggagePassengerRequestDto luggagePassengerRequestDto) {
          return luggagePassengerService.add(luggagePassengerRequestDto);
     }

     @GetMapping("/getList")
     public ApiResponse getList(){
          return luggagePassengerService.getList();
     }

     @GetMapping("/getById/{id}")
     public ApiResponse getById(@PathVariable UUID id){
          return luggagePassengerService.getById(id);
     }

     @GetMapping("/getListByFilter/{fromRegionId}/{toRegionId}/{fromCityId}/{toCityId}/{timeToLeave}")
     public ApiResponse getListByFilter(@PathVariable Integer fromRegionId,
                                        @PathVariable Integer toRegionId,
                                        @PathVariable Integer fromCityId,
                                        @PathVariable Integer toCityId,
                                        @PathVariable String timeToLeave
     ){
          return luggagePassengerService.getByFilter(fromRegionId,toRegionId,fromCityId,toCityId,timeToLeave);
     }

     @GetMapping("/getListByFilter/{fromRegionId}/{toRegionId}/{timeToLeave}")
     public ApiResponse getByFilter(@PathVariable Integer fromRegionId,
                                        @PathVariable Integer toRegionId,
                                        @PathVariable String timeToLeave
     ){
          return luggagePassengerService.getByFilter(fromRegionId,toRegionId,timeToLeave);
     }

     @DeleteMapping("/ofActive/{id}")
     public ApiResponse ofActive(@PathVariable UUID id){
          return luggagePassengerService.ofActive(id);
     }

}
