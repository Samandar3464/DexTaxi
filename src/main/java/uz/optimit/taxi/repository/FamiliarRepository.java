package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Familiar;

import java.util.List;
import java.util.UUID;

public interface FamiliarRepository extends JpaRepository<Familiar, UUID> {

    List<Familiar> findAllByUserId(UUID user_id);
}
