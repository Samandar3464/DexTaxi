package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.service.SeatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDriverRegisterRequestDto {

     private Integer fromRegionId;
     private Integer toRegionId;
     private Integer fromCityId;
     private Integer toCityId;
     private LocalDateTime timeToDrive;
     private double frontSeatPrice;
     private double backSeatPrice;
     private boolean baggage;
     private String info;
     private List<UUID> seatIdList;

}
