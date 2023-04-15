package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementDriverService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver")
public class AnnouncementDriverController {

    private final AnnouncementDriverService announcementDriverService;

    @PostMapping("/add")
    //    @PreAuthorize("hasRole('HAYDOVCHI')")
    public ApiResponse addDriverAnnouncement(@RequestBody AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto){
        return announcementDriverService.add(announcementDriverRegisterRequestDto);
    }

    @GetMapping("/getById/{id}")
    public ApiResponse getById(@PathVariable("id")UUID id){
        return announcementDriverService.getById(id);
    }

    @GetMapping("/getListForAnonymousUser")
    public ApiResponse getDriverListForAnonymousUser(){
        return announcementDriverService.getDriverListForAnonymousUser();
    }

    @GetMapping("/getDriverAnnouncements")
    public ApiResponse getDriverAnnouncements(){
        return announcementDriverService.getDriverAnnouncements();
    }

    @DeleteMapping("/deleteDriverAnnouncements/{id}")
    public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id){
        return announcementDriverService.deleteDriverAnnouncement(id);
    }
}
