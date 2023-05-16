package uz.optimit.taxi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Car;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
     Page<Car> findAllByActiveAndDeny(boolean Active,boolean deny, Pageable page);

     Optional<Car> findByUserIdAndActive(UUID user_id,boolean active);
     Optional<Car> findByUserId(UUID userId);
}
