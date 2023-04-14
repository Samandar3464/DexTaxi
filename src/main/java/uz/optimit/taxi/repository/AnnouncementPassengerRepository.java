package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {
     Optional<AnnouncementPassenger> findByIdAndActive(UUID id,boolean Active);

     Optional<AnnouncementPassenger> findByActiveAndUserId(boolean active, UUID user_id);

     List<AnnouncementPassenger> findByActive(boolean Active);

    List<AnnouncementDriver> findAllByActiveAndUserId(boolean b, UUID id);
}
