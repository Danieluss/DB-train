package db.train.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Train {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<Carriage> carriages;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trains_connections",
            joinColumns = { @JoinColumn(name = "train_id") },
            inverseJoinColumns = { @JoinColumn(name = "connection_id") })
    private Set<Connection> connections = new LinkedHashSet<>();
}
