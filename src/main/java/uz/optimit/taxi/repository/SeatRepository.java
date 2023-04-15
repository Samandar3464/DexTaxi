package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Seat;

import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
}
