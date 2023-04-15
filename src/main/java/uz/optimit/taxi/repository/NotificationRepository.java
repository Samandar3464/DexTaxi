package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByIdAndActive(UUID id, boolean active);

    List<Notification> findAllBySenderIdAndActiveAndReceived(UUID senderId, boolean active, boolean received);

    List<Notification> findAllByReceiverIdAndActiveAndReceived(UUID receiverId, boolean active, boolean received);

    Optional<Notification> findBySenderIdAndReceiverIdAndActiveAndReceived(UUID senderId, UUID receiverId, boolean active, boolean received);

    Optional<Notification> findFirstByReceiverIdAndReceivedTrueOrderByCreatedTimeDesc(UUID receiverId);

}
