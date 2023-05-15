package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.CountMassage;

import java.time.LocalDateTime;
import java.util.List;

public interface CountMassageRepository extends JpaRepository<CountMassage, Integer> {
    Integer countAllByCount(int count);

    Integer countAllBySandedTimeBetween(LocalDateTime sandedTime, LocalDateTime sandedTime2);

}
