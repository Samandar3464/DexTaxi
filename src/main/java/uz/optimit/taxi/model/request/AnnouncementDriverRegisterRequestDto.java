package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.service.SeatService;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDriverRegisterRequestDto {

     private Integer fromRegionId;
     private Integer toRegionId;
     private LocalDateTime timeToDrive;
     private double frontSeatPrice;
     private double backSeatPrice;
     private boolean baggage;
     private String info;

}
