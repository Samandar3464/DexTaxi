package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Region;

import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByName(String name);
}
