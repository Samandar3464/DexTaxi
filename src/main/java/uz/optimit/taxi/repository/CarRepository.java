package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
     List<Car> findAllByActive(boolean Active);
}
