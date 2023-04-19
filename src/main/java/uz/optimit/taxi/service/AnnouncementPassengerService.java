package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementAlreadyExistException;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponse;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementPassengerService {

    private final AnnouncementPassengerRepository repository;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final UserService userService;
    private  final  AttachmentService attachmentService;
    private final FamiliarRepository familiarRepository;


    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        Optional<AnnouncementPassenger> byUserIdAndActive = repository.findByUserIdAndActive(user.getId(), true);
        if (byUserIdAndActive.isPresent()){
            throw new AnnouncementAlreadyExistException(YOU_ALREADY_HAVE_ACTIVE_ANNOUNCEMENT);
        }
        AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto, user, regionRepository, cityRepository,familiarRepository);
        repository.save(announcementPassenger);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerListForAnonymousUser() {

        List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
        List<AnnouncementPassenger> allByActive = repository.findAllByActive(true);
        allByActive.forEach(a -> {
            passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a));
        });
        return new ApiResponse(passengerResponses, true);
    }

    @ResponseStatus(HttpStatus.FOUND)
    public ApiResponse getAnnouncementById(UUID id) {
        AnnouncementPassenger active = repository.findByIdAndActive(id, true).orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        AnnouncementPassengerResponse passengerResponse =
                AnnouncementPassengerResponse.from(active, attachmentService.attachDownloadUrl);
        return new ApiResponse(passengerResponse, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerAnnouncements() {
        User user = userService.checkUserExistByContext();

        List<AnnouncementPassenger> announcementPassengers = repository.findAllByActiveAndUserId(true, user.getId());
        return new ApiResponse(announcementPassengers, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deletePassengerAnnouncement(UUID id) {
        AnnouncementPassenger announcementPassenger = repository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        announcementPassenger.setActive(false);
        repository.save(announcementPassenger);
        return new ApiResponse(DELETED, true);
    }


    @ResponseStatus(HttpStatus.FOUND)
    public ApiResponse findFilter(
        Integer fromRegion,
        Integer toRegion,
        LocalDateTime timeToTravel,
        LocalDateTime toTime
        ){
        List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
        List<AnnouncementPassenger> byFilter = repository.findAllByActiveAndFromRegionIdAndToRegionIdAndTimeToTravelBetweenOrderByCreatedTimeDesc(true,fromRegion,toRegion,timeToTravel,toTime);
        byFilter.forEach(a -> {
            passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a));
        });
        return new ApiResponse(passengerResponses,true);
    }
}
