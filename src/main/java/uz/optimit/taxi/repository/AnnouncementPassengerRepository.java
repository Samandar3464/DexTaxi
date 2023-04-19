package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {
     Optional<AnnouncementPassenger> findByIdAndActive(UUID id,boolean Active);

     Optional<AnnouncementPassenger> findByUserIdAndActive(UUID user_id, boolean active);

     List<AnnouncementPassenger> findAllByActive(boolean Active);
//     List<AnnouncementPassenger> findAllByActiveAndOrderByCreatedTime(boolean Active);

    List<AnnouncementPassenger> findAllByActiveAndUserId(boolean b, UUID id);
}
