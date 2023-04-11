package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoModel;

import java.util.UUID;

public interface AutoModelRepository extends JpaRepository<AutoModel, UUID> {
    AutoModel getByIdAndAutoCategoryId(UUID id, UUID autoCategory_id);
}
