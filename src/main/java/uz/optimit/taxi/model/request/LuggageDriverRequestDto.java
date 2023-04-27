package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LuggageDriverRequestDto {

     private String cargoDescription;

     private LocalDateTime timeToLeave;

     private Integer fromRegionId;

     private Integer toRegionId;

     private Integer fromCityId;

     private Integer toCityId;
}
