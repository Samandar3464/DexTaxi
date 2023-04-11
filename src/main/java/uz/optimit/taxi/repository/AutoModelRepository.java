package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoModel;

import java.util.Optional;
import java.util.UUID;

public interface AutoModelRepository extends JpaRepository<AutoModel, Integer> {
    AutoModel getByIdAndAutoCategoryId(UUID id, UUID autoCategory_id);

    Optional<AutoModel> findByNameAndAutoCategoryId(String name, Integer autoCategory_id);
}
