package uz.optimit.taxi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Car;


import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
     Page<Car> findAllByActiveFalseAndDeletedFalse(Pageable page);

     Optional<Car> findByUserIdAndActive(UUID user_id,boolean active);
     Optional<Car> findByUserId(UUID userId);
}
