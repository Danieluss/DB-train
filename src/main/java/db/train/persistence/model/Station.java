package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Entity
public class Station {
    @Id
    private Long id;
    @Column(nullable = false)
    private String place;
    @OneToMany(mappedBy = "station1")
    @MapKey(name = "station2")
    private Map<Station, Edge> edges = new LinkedHashMap<>();
    private Double longitude;
    private Double latitude;
}
