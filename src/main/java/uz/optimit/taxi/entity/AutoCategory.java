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
public class AutoCategory {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "autoCategory" , cascade = CascadeType.ALL)
    private List<AutoModel> autoModel;
}
