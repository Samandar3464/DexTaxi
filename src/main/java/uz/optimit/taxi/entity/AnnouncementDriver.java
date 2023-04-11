package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AnnouncementDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Region fromRegion;
    @ManyToOne
    private Region toRegion;
    private double fromLatitude;
    private double fromLongitude;
    private double toLongitude;
    private double toLatitude;
    private double frontSeatPrice;
    private double backSeatPrice;
    private boolean baggage;
    private boolean isActive;
    private byte emptySeat;
    private LocalDateTime createdTime;
    private String info;

}
