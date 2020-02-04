package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Map;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "edge_gen", sequenceName = "edge_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Edge {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("distance", "Distance in kilometers")
            .put("station1", "From station")
            .put("station2", "To station")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "edge_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    @DecimalMin("0")
    @DecimalMax("100000")
    private Double distance;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station1_id")
    private Station station1;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station2_id")
    private Station station2;

    @JsonProperty("station1")
    public void setStation1(Long id) {
        station1 = new Station();
        station1.setId(id);
    }

    @JsonProperty("station2")
    public void setStation2(Long id) {
        station2 = new Station();
        station2.setId(id);
    }

}
