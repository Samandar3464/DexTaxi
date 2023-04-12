package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
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
          if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
               throw new UserNotFoundException("User not found");
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException("user not found"));
          AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto, user, regionRepository,cityRepository);
          repository.save(announcementPassenger);
          return new ApiResponse("Successfully", true);
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
     public ApiResponse getPassengerList(UUID id) {
          AnnouncementPassenger allByActive = repository.findByIdAndActive(id, true);
          AnnouncementPassengerResponse passengerResponse =
              AnnouncementPassengerResponse.from(allByActive, attachmentService.attachDownloadUrl);

          return new ApiResponse(passengerResponse, true);
     }

}
