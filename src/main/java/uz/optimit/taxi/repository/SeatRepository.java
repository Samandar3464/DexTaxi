package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Seat;

import java.util.UUID;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
     List<Seat> findByIdIn(List<UUID> uuidList);
     List<Seat> findAllByCar_Id(UUID carID);
     List<Seat> findAllByCar_IdAndActive(UUID car_id, boolean active);
}
