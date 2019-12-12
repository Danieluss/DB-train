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
    @Id
    private Long id;
    @ManyToMany(mappedBy = "connections")
    private List<Train> trains;
    @OneToMany(mappedBy = "connection")
    @OrderBy(value = "number")
    private Set<StationsConnections> stations = new LinkedHashSet<>();
}
