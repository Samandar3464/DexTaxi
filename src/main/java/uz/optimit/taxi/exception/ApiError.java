package uz.optimit.taxi.exception;
import java.time.LocalDateTime;
public record ApiError(
        String path,
        String message,
        int statusCode,
       String massage) {
}
