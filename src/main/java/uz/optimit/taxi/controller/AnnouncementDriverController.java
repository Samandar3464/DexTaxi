package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementDriverService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver")
public class AnnouncementDriverController {

    private final AnnouncementDriverService announcementDriverService;

    @PostMapping("/add")
    public ResponseEntity<?> addDriverAnnouncement(@RequestBody AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto){
        return announcementDriverService.add(announcementDriverRegisterRequestDto);
    }

    @GetMapping("/getList")
    public ResponseEntity<?> getDriverList(){
        return announcementDriverService.getDriverList();
    }
    @GetMapping("/getListForAnonymousUser")
    public ResponseEntity<?> getDriverListForAnonymousUser(){
        return announcementDriverService.getDriverListForAnonymousUser();
    }


}
