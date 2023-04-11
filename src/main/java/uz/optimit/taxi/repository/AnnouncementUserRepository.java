package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.util.UUID;

public interface AnnouncementUserRepository extends JpaRepository<AnnouncementPassenger, UUID> {
}
