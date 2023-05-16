package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseListForAdmin {

    private List<CarResponseDto> carResponseDtoList;
    private long allSize;
    private int allPage;
    private int currentPage;
}
