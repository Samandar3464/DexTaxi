package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.util.List;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {
     AnnouncementPassenger findByIdAndActive(UUID id,boolean Active);
     List<AnnouncementPassenger> findAllByActive(boolean Active);
}
