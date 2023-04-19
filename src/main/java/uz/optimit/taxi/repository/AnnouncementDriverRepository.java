package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementDriverRepository extends JpaRepository<AnnouncementDriver, UUID> {
    List<AnnouncementDriver> findAllByActive(boolean active);
    List<AnnouncementDriver> findAllByActiveAndFromRegionIdAndToRegionIdAndTimeToDriveBetweenOrderByCreatedTimeDesc(boolean active, Integer fromRegion, Integer toRegion, LocalDateTime fromTime, LocalDateTime toTime);

    List<AnnouncementDriver> findAllByUserIdAndActive(UUID user_id, boolean active);

    Optional<AnnouncementDriver> findByUserIdAndActive(UUID user_id, boolean active);

    Optional<AnnouncementDriver> findByIdAndActive(UUID announcementId, boolean b);
}
