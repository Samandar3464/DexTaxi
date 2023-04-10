package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoCategory;

import java.util.UUID;

public interface AutoCategoryRepository extends JpaRepository<AutoCategory, UUID> {
}
