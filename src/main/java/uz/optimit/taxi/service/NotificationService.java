package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.NotEnoughSeat;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponseAnonymous;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.NotificationRepository;

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
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createNotificationForDriver(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        announcementDriverRepository.findByIdAndActive(notificationRequestDto.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        Notification notification = Notification.from(notificationRequestDto, user);
        notificationRepository.save(notification);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createNotificationForPassenger(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        announcementPassengerRepository.findByIdAndActive(notificationRequestDto.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        Notification notification = Notification.from(notificationRequestDto, user);
        notificationRepository.save(notification);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerPostedNotification() {
        User user = userService.checkUserExistByContext();
        List<Notification> notifications = notificationRepository.findAllBySenderIdAndActiveAndReceived(user.getId(), true, false);

        List<AnnouncementDriver> announcementDrivers = new ArrayList<>();
        notifications.forEach(obj -> announcementDrivers.add(announcementDriverRepository.findByIdAndActive(obj.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementDriverResponseAnonymous> anonymousList = new ArrayList<>();
        announcementDrivers.forEach(obj -> anonymousList.add(AnnouncementDriverResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverPostedNotification() {
        User user = userService.checkUserExistByContext();

        List<Notification> notification = notificationRepository
                .findAllBySenderIdAndActiveAndReceived(user.getId(), true, false);

        List<AnnouncementPassenger> announcementPassengers = new ArrayList<>();
        notification.forEach(obj -> announcementPassengers.add(announcementPassengerRepository.findByIdAndActive(obj.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementPassengerResponseAnonymous> anonymousList = new ArrayList<>();
        announcementPassengers.forEach(obj -> anonymousList.add(AnnouncementPassengerResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse seeNotification() {
        User user = userService.checkUserExistByContext();

        List<Notification> notifications = notificationRepository
                .findAllByReceiverIdAndActiveAndReceived(user.getId(), true, false);

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        notifications.forEach(obj -> userResponseDtoList.add(
                UserResponseDto.from(userService.checkUserExistById(obj.getSenderId()), AttachmentService.attachDownloadUrl)));

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
        User user = userService.checkUserExistByContext();
        User driver = userService.checkUserExistById(userId);

//        Notification fromDriverToUser = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceived(driver.getId(), user.getId(), true, false)
//                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));

        Notification fromDriverToUser = getNotification(user,driver);

        AnnouncementPassenger announcementPassenger = getAnnouncementPassenger(fromDriverToUser);

        AnnouncementDriver announcementDriver = getAnnouncementDriver(driver);

        announcementPassenger.setActive(false);
        if (announcementDriver.getEmptySeat() < announcementPassenger.getForFamiliar()) {
            throw new NotEnoughSeat(NOT_ENOUGH_SEAT);
        }
        int emptySeat = announcementDriver.getEmptySeat() - announcementPassenger.getForFamiliar();
        announcementDriver.setEmptySeat((byte) emptySeat);
        if (emptySeat == 0) {
            announcementDriver.setActive(false);
        }
        fromDriverToUser.setReceived(true);
        fromDriverToUser.setActive(false);
        notificationRepository.save(fromDriverToUser);
        announcementDriverRepository.save(announcementDriver);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(YOU_ARE_ACCEPTED_REQUEST, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse acceptPassengerRequest(UUID userId) {
        User driver = userService.checkUserExistByContext();
        User passenger = userService.checkUserExistById(userId);


        Notification fromUserToDriver = getNotification(driver, passenger);

//        Notification fromUserToDriver = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceived(passenger.getId(), driver.getId(), true, false)
//                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));

        AnnouncementDriver announcementDriver = getAnnouncementDriver(fromUserToDriver);

        AnnouncementPassenger announcementPassenger = getAnnouncementPassenger(passenger);

        announcementPassenger.setActive(false);
        int emptySeat = announcementDriver.getEmptySeat() - 1;
        announcementDriver.setEmptySeat((byte) emptySeat);
        if (emptySeat == 0) {
            announcementDriver.setActive(false);
        }
        fromUserToDriver.setReceived(true);
        fromUserToDriver.setActive(false);
        notificationRepository.save(fromUserToDriver);
        announcementDriverRepository.save(announcementDriver);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(YOU_ARE_ACCEPTED_REQUEST, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAcceptedNotification() {
        User receiver = userService.checkUserExistByContext();

        Notification notification = notificationRepository.findFirstByReceiverIdAndReceivedTrueOrderByCreatedTimeDesc(receiver.getId())
                .orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));

        User sender =userService.checkUserExistById(notification.getSenderId());
        return new ApiResponse(UserResponseDto.from(sender, AttachmentService.attachDownloadUrl), true);
    }

    private Notification getNotification(User user1, User user2) {
        return notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceived(user2.getId(), user1.getId(), true, false)
                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));
    }

    private AnnouncementDriver getAnnouncementDriver(Notification fromUserToDriver) {
        return announcementDriverRepository
                .findByIdAndActive(fromUserToDriver.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }
    private AnnouncementDriver getAnnouncementDriver(User driver) {
        return announcementDriverRepository.findByUserIdAndActive( driver.getId(),true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }
    private AnnouncementPassenger getAnnouncementPassenger(Notification fromDriverToUser) {
        return announcementPassengerRepository.findByIdAndActive(fromDriverToUser.getAnnouncementId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }
    private AnnouncementPassenger getAnnouncementPassenger(User passenger) {
        return announcementPassengerRepository
                .findByUserIdAndActive(passenger.getId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }

}
