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
public class AnnouncementPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    private Region fromRegion;
    @ManyToOne
    private Region toRegion;

    @ManyToOne
    private User user;
    private double fromLatitude;
    private double fromLongitude;
    private double toLongitude;
    private double toLatitude;
    private double price;
    private boolean baggage;
    private boolean isActive;
    private boolean forFamiliar;
    private String info;
    private LocalDateTime createdTime;


}
