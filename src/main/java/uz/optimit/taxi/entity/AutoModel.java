package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AutoModel {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    @ManyToOne
    private AutoCategory autoCategory;

    @OneToMany(mappedBy = "autoModel")
    private List<Car> car;
}
