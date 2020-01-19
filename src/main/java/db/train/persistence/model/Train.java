package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "train_gen", sequenceName = "train_seq", initialValue = 1000)
public class Train {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<Carriage> carriages;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trains_connections",
            joinColumns = {@JoinColumn(name = "train_id")},
            inverseJoinColumns = {@JoinColumn(name = "connection_id")})
    private List<Connection> connections;

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
        }).collect(Collectors.toList());
    }

}
