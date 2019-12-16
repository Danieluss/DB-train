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
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany
    @JoinColumn(name = "train_id")
    private List<Carriage> carriages;
    @ManyToMany
    @JoinTable(
            name = "trains_connections",
            joinColumns = { @JoinColumn(name = "train_id") },
            inverseJoinColumns = { @JoinColumn(name = "connection_id") })
    private Set<Connection> connections = new LinkedHashSet<>();
}
