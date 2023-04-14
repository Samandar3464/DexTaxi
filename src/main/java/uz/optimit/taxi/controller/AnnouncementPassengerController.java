package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementPassengerService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passenger")
public class AnnouncementPassengerController {

     private final AnnouncementPassengerService announcementPassengerService;

     @PostMapping("/add")
     public ApiResponse addPassengerAnnouncement(@RequestBody AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
          return announcementPassengerService.add(announcementPassengerRegisterRequestDto);
     }

     @GetMapping("/getById/{id}")
     public ApiResponse getPassengerById(@PathVariable("id")UUID id) {
          return announcementPassengerService.getAnnouncementById(id);
     }

     @GetMapping("/getListForAnonymousUser")
     public ApiResponse getPassengerListForAnonymousUser() {
          return announcementPassengerService.getPassengerListForAnonymousUser();
     }

     @GetMapping("/getPassengerAnnouncements")
     public ApiResponse getPassengerAnnouncements(){
          return announcementPassengerService.getPassengerAnnouncements();
     }

     @DeleteMapping("/deleteDriverAnnouncements/{id}")
     public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id){
          return announcementPassengerService.deletePassengerAnnouncement(id);
     }
}
