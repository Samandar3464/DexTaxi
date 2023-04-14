package uz.optimit.taxi.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Familiar;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamiliarRepository extends JpaRepository<Familiar, UUID> {

    List<Familiar> findAllByUserId(UUID user_id);

    boolean existsByPhoneAndUserId(@Size(min = 9, max = 9) String phone, UUID user_id);
}
