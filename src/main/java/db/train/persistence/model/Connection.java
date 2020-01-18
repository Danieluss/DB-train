package db.train.persistence.model;

import db.train.persistence.model.join.StationsConnections;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Connection {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "connections")
    private List<Train> trains;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "connection")
    @OrderBy(value = "number")
    private Set<StationsConnections> stations = new LinkedHashSet<>();
}
