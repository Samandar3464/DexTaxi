package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;

import java.util.List;
import java.util.UUID;

public interface AnnouncementDriverRepository extends JpaRepository<AnnouncementDriver, UUID> {
     List<AnnouncementDriver> findAllByActive(boolean Active);
     AnnouncementDriver findAllById(UUID id);
}
