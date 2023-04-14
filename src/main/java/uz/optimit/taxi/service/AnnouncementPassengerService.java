package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponse;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementPassengerService {

     private final AnnouncementPassengerRepository repository;
     private final RegionRepository regionRepository;
     private final CityRepository cityRepository;
     private final UserRepository userRepository;
     private final AttachmentService attachmentService;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse add(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
          AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto, user, regionRepository,cityRepository);
          repository.save(announcementPassenger);
          return new ApiResponse(SUCCESSFULLY, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getPassengerListForAnonymousUser() {

          List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
          List<AnnouncementPassenger> allByActive = repository.findByActive(true);
          allByActive.forEach(a -> {
               passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a));
          });
          return new ApiResponse(passengerResponses, true);
     }

     @ResponseStatus(HttpStatus.FOUND)
     public ApiResponse getAnnouncementById(UUID id) {
          AnnouncementPassenger active = repository.findByIdAndActive(id, true).orElseThrow(()-> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
          AnnouncementPassengerResponse passengerResponse =
              AnnouncementPassengerResponse.from(active, attachmentService.attachDownloadUrl);
          return new ApiResponse(passengerResponse, true);
     }


     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getPassengerAnnouncements() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User user = (User) authentication.getPrincipal();
          List<AnnouncementDriver> announcementDrivers = repository.findAllByActiveAndUserId(true,user.getId());
          return new ApiResponse(announcementDrivers, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse deletePassengerAnnouncement(UUID id){
          AnnouncementPassenger announcementPassenger = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
          announcementPassenger.setActive(false);
          repository.save(announcementPassenger);
          return new ApiResponse(DELETED ,true);

     }
}
