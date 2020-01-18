package db.train.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
public class Zone {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "zones_connections",
            joinColumns = { @JoinColumn(name = "zone_id") },
            inverseJoinColumns = { @JoinColumn(name = "connection_id") })
    private Set<Connection> connections = new LinkedHashSet<>();
}
