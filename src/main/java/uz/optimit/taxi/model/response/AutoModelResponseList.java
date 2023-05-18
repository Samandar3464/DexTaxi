package uz.optimit.taxi.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AutoModel;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoModelResponseList {
    private List<AutoModel> autoModelList;
    private long allSize;
    private int allPage;
    private int currentPage;

}
