package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse addPassengerAnnouncement(@RequestBody AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
          return announcementPassengerService.add(announcementPassengerRegisterRequestDto);
     }

     @GetMapping("/getById/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getPassengerById(@PathVariable("id") UUID id) {
          return announcementPassengerService.getAnnouncementById(id);
     }

     @GetMapping("/getListForAnonymousUser")
     public ApiResponse getPassengerListForAnonymousUser() {
          return announcementPassengerService.getPassengerListForAnonymousUser();
     }

     @GetMapping("/getPassengerAnnouncements")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getPassengerAnnouncements() {
          return announcementPassengerService.getPassengerAnnouncements();
     }

     @DeleteMapping("/deletePassengerAnnouncements/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id) {
          return announcementPassengerService.deletePassengerAnnouncement(id);
     }

     @GetMapping("/getAnnouncementPassengerByFilter/{fromRegion}/{toRegion}/{timeToTravel}")
     public ApiResponse getAnnouncementPassengerByFilter(
         @PathVariable Integer fromRegion,
         @PathVariable Integer toRegion,
         @PathVariable String timeToTravel
     ) {
          return announcementPassengerService.findFilter(timeToTravel+" 23:59", toRegion, fromRegion, timeToTravel+" 00:01");
     }

     @GetMapping("/getPassengerAnnouncementHistory")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getDriverAnnouncementHistory(){
          return announcementPassengerService.getHistory();
     }
}
