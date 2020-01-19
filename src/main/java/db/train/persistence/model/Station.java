package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "station_gen", sequenceName = "station_seq", initialValue = 1000)
public class Station {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_gen")
    @Id
    @Access(AccessType.PROPERTY)
    private Long id;
    @Column(nullable = false)
    private String place;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"station1"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station1")
    private List<Edge> edges;
    private Double longitude;
    private Double latitude;

    @JsonProperty("edges")
    public void setEdges(List<Long> ids) {
        edges = ids.stream().map(id -> {
            Edge edge = new Edge();
            edge.setId(id);
            return edge;
        }).collect(Collectors.toList());
    }

}
