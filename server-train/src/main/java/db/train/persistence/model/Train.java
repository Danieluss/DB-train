package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "train_gen", sequenceName = "train_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Train {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Name of the train")
            .put("carriages", "Attached carriages")
            .put("connections", "Connections")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "train")
    @Size(min = 1)
    private List<Carriage> carriages;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trains_connections",
            joinColumns = {@JoinColumn(name = "train_id")},
            inverseJoinColumns = {@JoinColumn(name = "connection_id")})
    private Set<Connection> connections;

    @JsonProperty("carriages")
    public void setCarriages(List<Long> ids) {
        carriages = ids.stream().map(id -> {
            Carriage carriage = new Carriage();
            carriage.setId(id);
            return carriage;
        }).collect(Collectors.toList());
    }

    @JsonProperty("connections")
    public void setConnections(List<Long> ids) {
        connections = ids.stream().map(id -> {
            Connection connections = new Connection();
            connections.setId(id);
            return connections;
        }).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return Objects.equals(id, train.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
