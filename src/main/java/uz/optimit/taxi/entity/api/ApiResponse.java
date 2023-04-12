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

    private Object date;

    public ApiResponse(String massage, boolean status) {
        this.massage = massage;
        this.status = status;
    }

    public ApiResponse( Object date,boolean status) {
        this.status = status;
        this.date = date;
    }
}
