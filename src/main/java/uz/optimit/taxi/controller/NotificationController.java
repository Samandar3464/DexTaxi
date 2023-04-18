package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AcceptDriverRequestDto;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.service.NotificationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/addNotificationToDriver")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse createNotificationToDriver(@RequestBody NotificationRequestDto notificationRequestDto) {
        return notificationService.createNotificationForDriver(notificationRequestDto);
    }
    @PostMapping("/addNotificationToPassenger")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse createNotificationToPassenger(@RequestBody NotificationRequestDto notificationRequestDto) {
        return notificationService.createNotificationForPassenger(notificationRequestDto);
    }

    @GetMapping("/getDriverNotification")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse getDriverPostedNotification(){
        return notificationService.getDriverPostedNotification();
    }

    @GetMapping("/getPassengerNotification")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse getPassengerPostedNotification(){
        return notificationService.getPassengerPostedNotification();
    }

    @GetMapping("/seeNotification")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse seeNotificationComeToPassenger(){
        return notificationService.seeNotification();
    }

    @DeleteMapping("/deleteNotification/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse deleteNotification(@PathVariable UUID id){
        return notificationService.deleteNotification(id);
    }

    @PostMapping("/acceptDiverRequest")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse joinDiverRequest(@RequestBody AcceptDriverRequestDto acceptDriverRequestDto){
        return notificationService.acceptDiverRequest(acceptDriverRequestDto);
    }

    @GetMapping("/acceptPassengerRequest/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse joinPassengerRequest(@PathVariable UUID id){
        return notificationService.acceptPassengerRequest(id);
    }
    @GetMapping("/getAcceptedNotifications/")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
    public ApiResponse getAcceptedNotifications(){
        return notificationService.getAcceptedNotification();
    }

}
