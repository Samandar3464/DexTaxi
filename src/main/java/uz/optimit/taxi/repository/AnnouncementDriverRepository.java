package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementDriverRepository extends JpaRepository<AnnouncementDriver, UUID> {
    List<AnnouncementDriver> findAllByActiveTrueAndTimeToDriveAfterOrderByCreatedTimeDesc(LocalDateTime timeToDrive);

    List<AnnouncementDriver> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndTimeToDriveAfterAndTimeToDriveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToDrive, LocalDateTime timeToDrive2, LocalDateTime timeToDrive3);

    List<AnnouncementDriver> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndTimeToDriveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, Integer fromCity_id, Integer toCity_id,  LocalDateTime timeToDrive2, LocalDateTime timeToDrive3);

    List<AnnouncementDriver> findAllByUserIdAndActiveOrderByCreatedTime(UUID user_id, boolean active);

    Optional<AnnouncementDriver> findByUserIdAndActive(UUID user_id, boolean active);

    Optional<AnnouncementDriver> findByIdAndActive(UUID announcementId, boolean b);
}
