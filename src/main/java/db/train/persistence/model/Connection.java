package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "connections")
    private List<Train> trains;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "connection")
    @OrderBy(value = "number")
    private Set<StationsConnections> stations = new LinkedHashSet<>();
}
