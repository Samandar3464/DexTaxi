package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final AnnouncementDriverRepository announcementDriverRepository;
    private final AnnouncementPassengerRepository announcementPassengerRepository;
    private final UserService userService;

    @Scheduled(cron = "0 0 * * *")
    public void deActivateOldAnnouncementsPassenger() {
        List<AnnouncementPassenger> allByActiveTrueAndTimeToDriveBefore = announcementPassengerRepository.findAllByActiveTrueAndTimeToTravelBefore(userService.getTime());
        allByActiveTrueAndTimeToDriveBefore.forEach(obj -> obj.setActive(false));
        announcementPassengerRepository.saveAll(allByActiveTrueAndTimeToDriveBefore);
    }

    @Scheduled(cron = "0 0 * * *")
    public void deActivateOldAnnouncementsDriver(){
        List<AnnouncementDriver> allByActiveTrueAndTimeToDriveBefore = announcementDriverRepository.findAllByActiveTrueAndTimeToDriveBefore(userService.getTime());
        allByActiveTrueAndTimeToDriveBefore.forEach(obj-> obj.setActive(false));
        announcementDriverRepository.saveAll(allByActiveTrueAndTimeToDriveBefore);
    }
}
