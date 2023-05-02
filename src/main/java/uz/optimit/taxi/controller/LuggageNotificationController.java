package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.LuggageNotificationRequestDto;
import uz.optimit.taxi.service.LuggageNotificationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/luggageNotification")
@RequiredArgsConstructor
public class LuggageNotificationController {

     private final LuggageNotificationService luggageNotificationService;

     @RequestMapping("/makeNotificationToSendToPassenger")
     public ApiResponse makeNotificationToSendToPassenger(@RequestBody LuggageNotificationRequestDto luggageNotificationRequestDto) {
          return luggageNotificationService.makeNotificationToSendToPassenger(luggageNotificationRequestDto);
     }

     @RequestMapping("/makeNotificationToSendToDriver")
     public ApiResponse makeNotificationToSendToDriver(@RequestBody LuggageNotificationRequestDto luggageNotificationRequestDto) {
          return luggageNotificationService.makeNotificationToSendToDriver(luggageNotificationRequestDto);
     }

     @RequestMapping("/receiveNotificationsSendByDriver")
     public ApiResponse receiveNotificationsSendByDriver() {
          return luggageNotificationService.receiveNotificationsSendByDriver();
     }

     @RequestMapping("/receiveNotificationsSendByPassenger")
     public ApiResponse receiveNotificationsSendByPassenger() {
          return luggageNotificationService.receiveNotificationsSendByPassenger();
     }

     @RequestMapping("/acceptRequestPassenger/{id}")
     public ApiResponse acceptRequestPassenger(@PathVariable UUID id) {
          return luggageNotificationService.acceptRequestPassenger(id);
     }

     @RequestMapping("/acceptRequestDriver/{id}")
     public ApiResponse acceptRequestDriver(@PathVariable UUID id) {
          return luggageNotificationService.acceptRequestDriver(id);
     }

     @RequestMapping("/changeToRead/{id}")
     public ApiResponse changeToRead(@PathVariable UUID id) {
          return luggageNotificationService.changeToRead(id);
     }

     @RequestMapping("/deleteNotification/{id}")
     public ApiResponse deleteNotification(@PathVariable UUID id) {
          return luggageNotificationService.deleteNotification(id);
     }

     @RequestMapping("/getAcceptedNotification")
     public ApiResponse getAcceptedNotification() {
          return luggageNotificationService.getAcceptedNotification();
     }
}
