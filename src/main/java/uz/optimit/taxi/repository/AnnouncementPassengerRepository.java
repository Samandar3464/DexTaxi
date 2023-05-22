package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {

    List<AnnouncementPassenger> findAllByUserIdAndActiveOrderByCreatedTime(UUID user_id, boolean active);

    List<AnnouncementPassenger> findAllByActiveTrueAndTimeToTravelAfterOrderByCreatedTimeDesc(LocalDateTime timeToTravel);

    List<AnnouncementPassenger> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndTimeToTravelAfterAndTimeToTravelBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToTravel, LocalDateTime timeToTravel2, LocalDateTime timeToTravel3);

    List<AnnouncementPassenger> findAllByActiveAndFromRegionIdAndToRegionIdAndTimeToTravelBetweenOrderByCreatedTimeDesc(boolean active, Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToTravel, LocalDateTime timeToTravel2);

    Optional<AnnouncementPassenger> findByIdAndActive(UUID id, boolean Active);

    Optional<AnnouncementPassenger> findByUserIdAndActive(UUID user_id, boolean active);

//    List<AnnouncementPassenger> findAllByActive(boolean Active);

    List<AnnouncementPassenger> findAllByUserIdAndActive(UUID id, boolean active);
}
