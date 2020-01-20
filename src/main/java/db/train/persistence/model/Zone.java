package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "zone_gen", sequenceName = "zone_seq", initialValue = 1000)
public class Zone {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "zones_connections",
            joinColumns = {@JoinColumn(name = "zone_id")},
            inverseJoinColumns = {@JoinColumn(name = "connection_id")})
    private List<Connection> connections;

    @JsonProperty("connections")
    public void setConnections(List<Long> ids) {
        connections = ids.stream().map(id -> {
            Connection connection = new Connection();
            connection.setId(id);
            return connection;
        }).collect(Collectors.toList());
    }

}
