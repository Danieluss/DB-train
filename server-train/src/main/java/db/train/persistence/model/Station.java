package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "station_gen", sequenceName = "station_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Station {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Name of the station")
            .put("edges", "Outgoing edges")
            .put("longitude", "Longitude")
            .put("latitude", "Latitude")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_gen")
    @Id
    @Access(AccessType.PROPERTY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"station1"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "station1_id")
    private List<Edge> edges;
    @DecimalMin("-180")
    @DecimalMax("180")
    private Double longitude;
    @DecimalMin("-90")
    @DecimalMax("90")
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
