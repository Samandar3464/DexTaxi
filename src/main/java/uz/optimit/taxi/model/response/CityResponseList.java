package uz.optimit.taxi.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.City;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseList {
    private List<City> autoModelList;
    private long allSize;
    private int allPage;
    private int currentPage;

}
