package uz.optimit.taxi.model.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public  List<Seat> createCarSeats(byte count , Car car){
        List<Seat> seatList  = new ArrayList<>();
        for (byte i = 1; i <= count; i++) {
            seatList.add(seatRepository.save(
                    Seat.builder()
                            .seatNumber(i)
                            .active(false)
                            .car(car)
                            .build()));
        }
        return seatList;
    }
}
