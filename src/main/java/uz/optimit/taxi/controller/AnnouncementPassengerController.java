package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementPassengerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passenger")
public class AnnouncementPassengerController {

    private final AnnouncementPassengerService announcementPassengerService;

    @PostMapping("/add")
    public ResponseEntity<?> addPassengerAnnouncement(@RequestBody AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto){
        return announcementPassengerService.add(announcementPassengerRegisterRequestDto);
    }

    @GetMapping("/getList")
    public ResponseEntity<?> getPassengerList(){
        return announcementPassengerService.getPassengerList();
    }
    @GetMapping("/getListForAnonymousUser")
    public ResponseEntity<?> getPassengerListForAnonymousUser(){
        return announcementPassengerService.getPassengerListForAnonymousUser();
    }

}
