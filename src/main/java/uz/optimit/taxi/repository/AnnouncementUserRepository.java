package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementUser;

import java.util.UUID;

public interface AnnouncementUserRepository extends JpaRepository<AnnouncementUser, UUID> {
}
