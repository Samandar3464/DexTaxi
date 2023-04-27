package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.LuggagePassenger;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface LuggagePassengerRepository extends JpaRepository<LuggagePassenger, UUID> {
     Optional<LuggagePassenger> findBySenderIdAndReceiverIdAndActive(UUID sender_id, UUID receiver_id, boolean active);

     List<LuggagePassenger> findAllByFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, Integer fromCity_id, Integer toCity_id, LocalDateTime timeToLeave, LocalDateTime timeToLeave2);
     List<LuggagePassenger> findAllByFromRegionIdAndToRegionIdAndTimeToLeaveBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToLeave, LocalDateTime timeToLeave2);
     List<LuggagePassenger> findAllByActive(boolean active);
     Optional<LuggagePassenger> findByIdAndActive(UUID id,boolean active);

}
