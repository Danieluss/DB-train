package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Entity
public class Station {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private String place;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station1")
    @MapKey(name = "station2")
    private Map<Station, Edge> edges = new LinkedHashMap<>();
    private Double longitude;
    private Double latitude;
}
