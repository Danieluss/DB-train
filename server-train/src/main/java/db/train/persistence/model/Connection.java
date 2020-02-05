package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.join.StationsConnections;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.webrepogen.annotations.ExcludedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;
import javax.validation.constraints.AssertTrue;

@Setter
@Getter
@ExcludedEntity
@Entity
@SequenceGenerator(name = "connection_gen", sequenceName = "connection_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Connection {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Name of the connection - eg. \"Rainbow bridge\"")
            .put("trains", "Operating trains")
            .put("stations", "Stations in sequence")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "connection_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private String firstStation;
    @Column(nullable = false)
    private String lastStation;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trains_connections",
            joinColumns = {@JoinColumn(name = "train_id")},
            inverseJoinColumns = {@JoinColumn(name = "connection_id")})
    private Set<Train> trains;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy(value = "number")
    @JoinColumn(name = "connection_id")
    private List<StationsConnections> stations;
    @Column(nullable = false)
    private Date firstDay;
    @Column(nullable = false)
    private Date lastDay;

    @JsonProperty("trains")
    public void setTrains(List<Long> ids) {
        trains = ids.stream().map(id -> {
            Train train = new Train();
            train.setId(id);
            return train;
        }).collect(Collectors.toSet());
    }

    @JsonProperty("stations")
    public void setStations(List<Long> ids) {
        stations = ids.stream().map(id -> {
            StationsConnections stations = new StationsConnections();
            stations.setId(id);
            return stations;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection connection = (Connection) o;
        return Objects.equals(id, connection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @AssertTrue(message="First day should be before last day.")
    private boolean isCrossValid() {
        return (this.firstDay.compareTo(this.lastDay) <= 0);
    }

}
