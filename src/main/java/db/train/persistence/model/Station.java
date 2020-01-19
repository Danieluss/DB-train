package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Station {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    @Access(AccessType.PROPERTY)
    private Long id;
    @Column(nullable = false)
    private String place;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonIgnoreProperties({"station1"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station1")
    private List<Edge> edges;
    private Double longitude;
    private Double latitude;
}
