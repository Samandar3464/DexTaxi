package uz.optimit.taxi.entity.api;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String massage;

    private boolean status;

    private Object data;

    public ApiResponse(String massage, boolean status) {
        this.massage = massage;
        this.status = status;
    }

    public ApiResponse(Object data, boolean status) {
        this.status = status;
        this.data = data;
    }
}
