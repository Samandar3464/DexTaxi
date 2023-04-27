package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.LuggageDriver;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface LuggageDriverRepository extends JpaRepository<LuggageDriver, UUID> {
     Optional<LuggageDriver> findBySupplierIdAndActive(UUID supplier_id, boolean active);
     List<LuggageDriver> findAllByActive(boolean active);
     List<LuggageDriver> findAllByFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, Integer fromCity_id, Integer toCity_id, LocalDateTime timeToLeave, LocalDateTime timeToLeave2);
     List<LuggageDriver> findAllByFromRegionIdAndToRegionIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToLeave, LocalDateTime timeToLeave2);
     Optional<LuggageDriver> findByIdAndActive(UUID id,boolean active);
}
