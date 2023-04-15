package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seat {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
     private int seatNumber;
     private double price;
     private boolean isBooked;

}
