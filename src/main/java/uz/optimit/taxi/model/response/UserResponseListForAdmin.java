package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseListForAdmin {

    private List<UserResponseDto> userResponseDtoList;
    private long allSize;
    private int allPage;
    private int currentPage;

}
