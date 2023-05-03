package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.CountMassage;

public interface CountMassageRepository extends JpaRepository<CountMassage, Integer> {
}
