package uz.optimit.taxi.model.request;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.Region;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LuggagePassengerRequestDto {

     private double price;

     private String cargoDescription;

     private LocalDateTime timeToLeave;

     private Integer fromRegionId;

     private Integer toRegionId;

     private Integer fromCityId;

     private Integer toCityId;

     private UUID senderId;

     private UUID receiverId;
}
