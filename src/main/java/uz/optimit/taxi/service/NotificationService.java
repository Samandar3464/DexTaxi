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
import uz.optimit.taxi.entity.Notification;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.NotEnoughSeat;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponseAnonymous;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.NotificationRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AnnouncementPassengerRepository announcementPassengerRepository;
    private final AnnouncementDriverRepository announcementDriverRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createNotificationForDriver(NotificationRequestDto notificationRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        announcementDriverRepository.findByIdAndActive(notificationRequestDto.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        Notification notification = Notification.from(notificationRequestDto, user);
        notificationRepository.save(notification);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createNotificationForPassenger(NotificationRequestDto notificationRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        announcementPassengerRepository.findByIdAndActive(notificationRequestDto.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        Notification notification = Notification.from(notificationRequestDto, user);
        notificationRepository.save(notification);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerPostedNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationRepository.findAllBySenderIdAndActiveAndReceivered(user.getId(), true, false);

        List<AnnouncementDriver> announcementDrivers = new ArrayList<>();
        notifications.forEach(obj -> announcementDrivers.add(announcementDriverRepository.findByIdAndActive(obj.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementDriverResponseAnonymous> anonymousList = new ArrayList<>();
        announcementDrivers.forEach(obj -> anonymousList.add(AnnouncementDriverResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverPostedNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        List<Notification> notification = notificationRepository
                .findAllBySenderIdAndActiveAndReceivered(user.getId(), true, false);

        List<AnnouncementPassenger> announcementPassengers = new ArrayList<>();
        notification.forEach(obj -> announcementPassengers.add(announcementPassengerRepository.findByIdAndActive(obj.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementPassengerResponseAnonymous> anonymousList = new ArrayList<>();
        announcementPassengers.forEach(obj -> anonymousList.add(AnnouncementPassengerResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse seeNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();

        List<Notification> notifications = notificationRepository
                .findAllByReceiverIdAndActiveAndReceivered(user.getId(), true, false);

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        notifications.forEach(obj -> userResponseDtoList.add(
                UserResponseDto.from(userRepository.findById(obj.getSenderId())
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)), attachmentService.attachDownloadUrl)));

        return new ApiResponse(userResponseDtoList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));
        notification.setActive(false);
        notificationRepository.save(notification);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse acceptDiverRequest(UUID userId) throws NotEnoughSeat {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        User driver = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Notification fromDriverToUser = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceivered(driver.getId(), user.getId(), true, false)
                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));

        AnnouncementPassenger announcementPassenger = announcementPassengerRepository.findByIdAndActive(fromDriverToUser.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));

        AnnouncementDriver announcementDriver = announcementDriverRepository.findByActiveAndUserId(true, driver.getId())
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));

        announcementPassenger.setActive(false);
        if (announcementDriver.getEmptySeat() < announcementPassenger.getForFamiliar()) {
            throw new NotEnoughSeat(NOT_ENOUGH_SEAT);
        }
        int emptySeat = announcementDriver.getEmptySeat() - announcementPassenger.getForFamiliar();
        announcementDriver.setEmptySeat((byte) emptySeat);
        if (emptySeat == 0) {
            announcementDriver.setActive(false);
        }
        fromDriverToUser.setReceivered(true);
        fromDriverToUser.setActive(false);
        notificationRepository.save(fromDriverToUser);
        announcementDriverRepository.save(announcementDriver);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(YOU_ARE_ACCEPTED_REQUEST, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse acceptPassengerRequest(UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User driver = (User) authentication.getPrincipal();
        User passenger = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Notification fromUserToDriver = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceivered(passenger.getId(), driver.getId(), true, false)
                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));

        AnnouncementDriver announcementDriver = announcementDriverRepository.findByIdAndActive(fromUserToDriver.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));

        AnnouncementPassenger announcementPassenger = announcementPassengerRepository.findByUserIdAndActive(passenger.getId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));

        announcementPassenger.setActive(false);
        int emptySeat = announcementDriver.getEmptySeat() - 1;
        announcementDriver.setEmptySeat((byte) emptySeat);
        if (emptySeat == 0) {
            announcementDriver.setActive(false);
        }
        fromUserToDriver.setReceivered(true);
        fromUserToDriver.setActive(false);
        notificationRepository.save(fromUserToDriver);
        announcementDriverRepository.save(announcementDriver);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(YOU_ARE_ACCEPTED_REQUEST, true);
    }

//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse getAcceptedNotification() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        User receiver = (User) authentication.getPrincipal();
//        Notification notification = notificationRepository.findByReceiverIdAndReceivered(receiver.getId(), true)
//                .orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
//        User sender = userRepository.findById(notification.getSenderId()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
//        return new ApiResponse(UserResponseDto.from(sender,AttachmentService.attachDownloadUrl ), true);
//    }
}
