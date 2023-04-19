package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementDriverService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver")
public class AnnouncementDriverController {

    private final AnnouncementDriverService announcementDriverService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse addDriverAnnouncement(@RequestBody AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto){
        return announcementDriverService.add(announcementDriverRegisterRequestDto);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getById(@PathVariable("id")UUID id){
        return announcementDriverService.getById(id);
    }

    @GetMapping("/getListForAnonymousUser")
    public ApiResponse getDriverListForAnonymousUser(){
        return announcementDriverService.getDriverListForAnonymousUser();
    }

    @GetMapping("/getDriverAnnouncements")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverAnnouncements(){
        return announcementDriverService.getDriverAnnouncements();
    }

    @DeleteMapping("/deleteDriverAnnouncements/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id){
        return announcementDriverService.deleteDriverAnnouncement(id);
    }

    @GetMapping("getAnnouncementDriverByFilter/{from}/{to}/{fromTime}/{toTime}/{size}")
    public ApiResponse getByFilter(
        @PathVariable Integer from,
        @PathVariable Integer to,
        @PathVariable LocalDateTime fromTime,
        @PathVariable LocalDateTime toTime,
        @PathVariable int size
    ){
       return announcementDriverService.getByFilter(from,to ,fromTime,toTime,size);
    }

    @GetMapping("getAnnouncementDriverByFilter/{from}/{to}/{fromTime}/{toTime}")
    public ApiResponse getByFilter(
        @PathVariable Integer from,
        @PathVariable Integer to,
        @PathVariable LocalDateTime fromTime,
        @PathVariable LocalDateTime toTime
    ){
        return announcementDriverService.getByFilter(from,to ,fromTime,toTime);
    }

    @GetMapping("/getDriverAnnouncementHistory")
    public ApiResponse getDriverAnnouncementHistory(){
        return announcementDriverService.getHistory();
    }
}
