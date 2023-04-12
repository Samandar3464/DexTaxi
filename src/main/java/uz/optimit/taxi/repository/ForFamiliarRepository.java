package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.ForFamiliar;

import java.util.Optional;
import java.util.UUID;

public interface ForFamiliarRepository extends JpaRepository<ForFamiliar, UUID> {

    Optional<ForFamiliar> findByIdAndUserId(UUID id, UUID user_id);
}
