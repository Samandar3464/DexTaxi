package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Region;

import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, Integer> {
     Optional<City> findByName(String name);
}
