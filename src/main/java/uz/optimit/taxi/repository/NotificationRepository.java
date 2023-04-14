package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByIdAndActive(UUID id, boolean active);
    List<Notification> findAllBySenderIdAndActiveAndReceivered(UUID senderId, boolean active, boolean receivered);

    List<Notification> findAllByReceiverIdAndActiveAndReceivered(UUID receiverId, boolean active, boolean receivered);

    Optional<Notification> findByActiveAndSenderIdAndReceiverId(boolean active, UUID senderId, UUID receiverId);
    Optional<Notification> findBySenderIdAndReceiverIdAndActiveAndReceivered(UUID senderId, UUID receiverId, boolean active, boolean receivered);

}
