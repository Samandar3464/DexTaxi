package uz.optimit.taxi.controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.service.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/region")
public class RegionController {

     private final RegionService regionService;

     @PostMapping("/add")
     //    @PreAuthorize("hasRole('ADMIN')")
     public ApiResponse addRegion(@RequestBody RegionRegisterRequestDto regionRegisterRequestDto) {
          return regionService.addRegion(regionRegisterRequestDto);
     }

     @GetMapping("/getRegionList")
     public ApiResponse getRegionList() {
          return regionService.getRegionList();
     }

     @GetMapping("/regionById{id}")
     public ApiResponse getRegionById(@PathVariable Integer id){
          return regionService.getRegionById(id);
     }
}
