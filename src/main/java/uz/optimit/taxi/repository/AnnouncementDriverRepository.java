package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementDriverRepository extends JpaRepository<AnnouncementDriver, UUID> {
     List<AnnouncementDriver> findAllByActive(boolean Active);

    List<AnnouncementDriver> findAllByActiveAndUserId(boolean b, UUID id);
    Optional<AnnouncementDriver> findByActiveAndUserId(boolean active, UUID user_id);

    Optional<AnnouncementDriver> findByIdAndActive(UUID announcementId, boolean b);
}
