package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;
import uz.optimit.taxi.entity.Enum.Constants;
import uz.optimit.taxi.entity.LuggageDriver;
import uz.optimit.taxi.entity.LuggagePassenger;
import uz.optimit.taxi.entity.Notification;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.LuggageAnnouncementNotFound;
import uz.optimit.taxi.model.request.LuggageNotificationRequestDto;
import uz.optimit.taxi.model.response.LuggageDriverResponse;
import uz.optimit.taxi.model.response.LuggagePassengerResponse;
import uz.optimit.taxi.model.response.NotificationMessageResponse;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.LuggageDriverRepository;
import uz.optimit.taxi.repository.LuggagePassengerRepository;
import uz.optimit.taxi.repository.NotificationRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LuggageNotificationService {

     private final NotificationRepository notificationRepository;
     private final LuggagePassengerRepository luggagePassengerRepository;
     private final AnnouncementPassengerRepository announcementPassengerRepository;
     private final LuggageDriverRepository luggageDriverRepository;
     private final UserService userService;
     private final AttachmentService attachmentService;
     private final FireBaseMessagingService fireBaseMessagingService;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse makeNotificationToSendToDriver(LuggageNotificationRequestDto luggageNotificationRequestDto) {

          User user = userService.checkUserExistByContext();
          LuggageDriver luggageDriver = luggageDriverRepository.findByIdAndActive(luggageNotificationRequestDto.getLuggageDriverAnnouncementId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND));
          LuggagePassenger luggagePassenger = luggagePassengerRepository.findByUserIdAndActive(user.getId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND));
          luggageNotificationRequestDto.setLuggagePassengerAnnouncementId(luggagePassenger.getId());
          luggageNotificationRequestDto.setTitle(Constants.YOU_HAVE_RECEIVED_A_MESSAGE_FROM_A_PASSENGER);

          Notification notification = from(luggageNotificationRequestDto, user, luggagePassenger, luggageDriver);
          UserResponseDto userResponseDto = UserResponseDto.fromAnnouncement(user, attachmentService.attachDownloadUrl);
          NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForDriver(luggageNotificationRequestDto, notification.getReceiverToken());
          notificationMessageResponse.setData(getData(userResponseDto));
          fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

          return new ApiResponse(Constants.SUCCESSFULLY, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse makeNotificationToSendToPassenger(LuggageNotificationRequestDto luggageNotificationRequestDto) {

          User user = userService.checkUserExistByContext();
          LuggagePassenger luggagePassenger = luggagePassengerRepository.findByIdAndActive(luggageNotificationRequestDto.getLuggagePassengerAnnouncementId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND));
          LuggageDriver luggageDriver = luggageDriverRepository.findBySupplierIdAndActive(user.getId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND));
          luggageNotificationRequestDto.setLuggageDriverAnnouncementId(luggageDriver.getId());
          luggageNotificationRequestDto.setTitle(Constants.YOU_HAVE_RECEIVED_A_MESSAGE_FROM_A_DRIVER);

          Notification notification = forDriver(luggageNotificationRequestDto, luggageDriver, luggagePassenger);
          UserResponseDto userResponseDto = UserResponseDto.fromAnnouncement(user, attachmentService.attachDownloadUrl);
          NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForPassenger(luggageNotificationRequestDto, notification.getReceiverToken());
          notificationMessageResponse.setData(getData(userResponseDto));
          fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

          return new ApiResponse(Constants.SUCCESSFULLY, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse receiveNotificationsSendByPassenger() {
          User user = userService.checkUserExistByContext();

          List<Notification> notifications = notificationRepository.findAllByUserIdAndActiveAndReceived(user.getId(), true, false);
          List<LuggagePassenger> luggagePassengerList = new ArrayList<>();
          notifications.forEach(notification -> luggagePassengerList.add(luggagePassengerRepository.findByIdAndActive(notification.getAnnouncementPassengerId(), true)
              .orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND))));

          List<LuggagePassengerResponse> luggagePassengerResponses = new ArrayList<>();
          luggagePassengerList.forEach(luggagePassenger -> {
               luggagePassengerResponses.add(LuggagePassengerResponse.from(luggagePassenger));
          });
          return new ApiResponse(luggagePassengerResponses, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse receiveNotificationsSendByDriver() {
          User user = userService.checkUserExistByContext();
          List<Notification> notifications = notificationRepository.findAllByUserIdAndActiveAndReceived(user.getId(), true, false);

          List<LuggageDriver> luggageDriverList = new ArrayList<>();
          notifications.forEach(notification -> luggageDriverList.add(luggageDriverRepository.findByIdAndActive(notification.getAnnouncementDriverId(), true)
              .orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND))));

          List<LuggageDriverResponse> luggageDriverResponses = new ArrayList<>();
          luggageDriverList.forEach(luggageDriver -> {
               luggageDriverResponses.add(LuggageDriverResponse.from(luggageDriver, attachmentService.attachDownloadUrl));
          });
          return new ApiResponse(luggageDriverResponses, true);
     }

//     @ResponseStatus(HttpStatus.OK)
//     public ApiResponse viewNotificationsSentByPassenger(){
//
//          User user = userService.checkUserExistByContext();
//          List<LuggageDriver> luggageDriver = luggageDriverRepository.findByUserId(user.getId());
//          List<LuggagePassenger> luggagePassengerList = luggagePassengerRepository.findAllByReceiverIdAndActive(luggageDriver.getId(), true);
//
//          List<LuggagePassengerResponse> luggagePassengerResponses = new ArrayList<>();
//          luggagePassengerList.forEach(luggagePassenger -> {
//               luggagePassengerResponses.add(LuggagePassengerResponse.from(luggagePassenger));
//          });
//          return new ApiResponse(luggagePassengerResponses,true);
//     }

     @ResponseStatus(HttpStatus.OK)
     @Transactional(rollbackFor = {})
     public ApiResponse acceptRequestDriver(UUID uuid) {

          User user = userService.checkUserExistByContext();
          User driver = userService.checkUserExistById(uuid);
          Notification fromDriverToUser = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceived(driver.getId(), user.getId(), true, false).orElseThrow(() -> new NotFoundException(Constants.NOTIFICATION_NOT_FOUND));
          extracted(driver,user,fromDriverToUser);
          return new ApiResponse(Constants.YOU_ACCEPTED_REQUEST, true);
     }

     @ResponseStatus(HttpStatus.OK)
     @Transactional(rollbackFor = {})
     public ApiResponse acceptRequestPassenger(UUID uuid) {

          User user = userService.checkUserExistByContext();
          User passenger = userService.checkUserExistById(uuid);
          Notification fromUserToDriver = notificationRepository.findBySenderIdAndReceiverIdAndActiveAndReceived(uuid, user.getId(), true, false).orElseThrow(() -> new NotFoundException(Constants.NOTIFICATION_NOT_FOUND));
          extracted(user, passenger, fromUserToDriver);
          return new ApiResponse();
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getAcceptedNotification(){

          User receiver = userService.checkUserExistByContext();
          Notification notification = notificationRepository.findFirstByReceiverIdAndReceivedTrueOrderByCreatedTimeDesc(receiver.getId()).orElseThrow(() -> new AnnouncementNotFoundException(Constants.NOTIFICATION_NOT_FOUND));
          User sender = userService.checkUserExistById(notification.getSenderId());
          return new ApiResponse(UserResponseDto.from(sender, attachmentService.attachDownloadUrl, announcementPassengerRepository),true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse changeToRead(UUID notificationId){
          Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException(Constants.NOTIFICATION_NOT_FOUND));
          notification.setRead(true);
          notificationRepository.save(notification);
          return new ApiResponse(Constants.SUCCESSFULLY,true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse deleteNotification(UUID id){
          Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.NOTIFICATION_NOT_FOUND));
          notification.setActive(false);
          notificationRepository.save(notification);
          return new ApiResponse(Constants.DELETED,true);
     }

     private void extracted(User user, User passenger, Notification fromUserToDriver) {
          LuggagePassenger luggagePassenger = luggagePassengerRepository.findByUserIdAndActive(passenger.getId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_PASSENGER_ANNOUNCEMENT_NOT_FOUND));
          LuggageDriver luggageDriver = luggageDriverRepository.findByUserIdAndActive(user.getId(), true).orElseThrow(() -> new LuggageAnnouncementNotFound(Constants.LUGGAGE_DRIVER_ANNOUNCEMENT_NOT_FOUND));

          fromUserToDriver.setActive(false);
          fromUserToDriver.setReceived(true);
          luggagePassenger.setActive(false);
          luggagePassenger.setSupplier(luggageDriver.getSupplier());
          notificationRepository.save(fromUserToDriver);
          luggagePassengerRepository.save(luggagePassenger);
     }

     private Map<String, String> getData(UserResponseDto userResponseDto) {
          Map<String, String> data = new HashMap<>();
          data.put("id", userResponseDto.getId().toString());
          data.put("name", userResponseDto.getId().toString());
          data.put("surname", userResponseDto.getId().toString());
          data.put("phone", userResponseDto.getId().toString());
          data.put("age", userResponseDto.getId().toString());
          data.put("status", userResponseDto.getId().toString());
          data.put("gender", userResponseDto.getId().toString());
          data.put("profilePhotoUrl", userResponseDto.getId().toString());
          return data;
     }

     private Notification from(LuggageNotificationRequestDto luggageNotificationRequestDto, User user, LuggagePassenger luggagePassenger, LuggageDriver luggageDriver) {

          Notification notification = Notification.fromLuggage(luggageNotificationRequestDto, luggageDriver.getUser().getId());
          notification.setSenderId(luggagePassenger.getUser().getId());
          notification.setUser(user);
          notification.setReceiverToken(luggageDriver.getSupplier().getFireBaseToken());
          return notificationRepository.save(notification);
     }

     private Notification forDriver(LuggageNotificationRequestDto luggageNotificationRequestDto, LuggageDriver luggageDriver, LuggagePassenger luggagePassenger) {

          Notification notification = Notification.fromLuggage(luggageNotificationRequestDto, luggagePassenger.getUser().getId());
          notification.setSenderId(luggageDriver.getUser().getId());
          notification.setUser(luggageDriver.getUser());
          notification.setReceiverToken(luggagePassenger.getSupplier().getFireBaseToken());
          return notificationRepository.save(notification);
     }
}
