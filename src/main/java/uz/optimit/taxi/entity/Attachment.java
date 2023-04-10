package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attachment  {
    @Id
    @GeneratedValue
    private UUID id;
    private String originName;
    private long size;
    private String newName;
    private String type;
    private String contentType;
    private double path;
    @ManyToMany
    private List<Car> car;
}
