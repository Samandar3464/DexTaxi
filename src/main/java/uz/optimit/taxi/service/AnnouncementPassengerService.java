package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
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
import java.time.format.DateTimeFormatter;
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
    private final AttachmentService attachmentService;
    private final FamiliarRepository familiarRepository;


    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        Optional<AnnouncementPassenger> byUserIdAndActive = repository.findByUserIdAndActive(user.getId(), true);
        if (byUserIdAndActive.isPresent()) {
            throw new AnnouncementAlreadyExistException(YOU_ALREADY_HAVE_ACTIVE_ANNOUNCEMENT);
        }
        AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto, user, regionRepository, cityRepository, familiarRepository);
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
                AnnouncementPassengerResponse.from(active);
        return new ApiResponse(passengerResponse, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerAnnouncements() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementPassenger> announcementPassengers = repository.findAllByUserIdAndActive(user.getId(),true);
        List<AnnouncementPassengerResponseAnonymous> anonymousList = new ArrayList<>();
        announcementPassengers.forEach(obj->
                anonymousList.add(AnnouncementPassengerResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
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
            String timeToTravel,
            String toTime
    ) {
        List<AnnouncementPassenger> byFilter = getAnnouncementPassengers(fromRegion, toRegion, timeToTravel, toTime);
        List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
        byFilter.forEach(a -> {
            passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a));
        });
        return new ApiResponse(passengerResponses, true);
    }

    private List<AnnouncementPassenger> getAnnouncementPassengers(Integer fromRegion, Integer toRegion, String timeToTravel, String toTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime timeToTravel1 = LocalDateTime.parse(timeToTravel,formatter);
        LocalDateTime toTime1 = LocalDateTime.parse(toTime,formatter);
        return  repository.findAllByActiveAndFromRegionIdAndToRegionIdAndTimeToTravelBetweenOrderByCreatedTimeDesc
            (true, fromRegion, toRegion, timeToTravel1, toTime1);
    }

    public ApiResponse getHistory() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementPassenger> allByActive = repository.findAllByUserIdAndActive(user.getId(),false);
        List<AnnouncementPassengerResponse> response = new ArrayList<>();
        allByActive.forEach((announcementPassenger)-> response.add(AnnouncementPassengerResponse
            .from(announcementPassenger)));
        return new ApiResponse(response,true);
    }
}
