package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementFamiliarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forFamiliar")
public class AnnouncementPassengerFamiliar {
     private final AnnouncementFamiliarService announcementFamiliarService;

     @PostMapping("/add")
     public ApiResponse add(@RequestBody FamiliarRegisterRequestDto  familiarRegisterRequestDto) {
         return announcementFamiliarService.addForFamiliar(familiarRegisterRequestDto);

     }
     @GetMapping("/getFamiliarList")
     public ApiResponse getList() {
          return announcementFamiliarService.getFamiliarListByUser();
     }

     @PostMapping("/getFamiliarListByIdList")
     public ApiResponse getListByIds(@RequestBody List<UUID> list) {
          return announcementFamiliarService.getFamiliarListByUserId(list);
     }

}
